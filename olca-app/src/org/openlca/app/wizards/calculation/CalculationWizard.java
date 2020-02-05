package org.openlca.app.wizards.calculation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.openlca.app.App;
import org.openlca.app.M;
import org.openlca.app.db.Database;
import org.openlca.app.editors.Editors;
import org.openlca.app.editors.ModelEditorInput;
import org.openlca.app.results.ResultEditorInput;
import org.openlca.app.results.Sort;
import org.openlca.app.results.analysis.AnalyzeEditor;
import org.openlca.app.results.quick.QuickResultEditor;
import org.openlca.app.results.simulation.SimulationEditor;
import org.openlca.app.util.Question;
import org.openlca.app.util.UI;
import org.openlca.core.database.FlowDao;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ProductSystemDao;
import org.openlca.core.math.CalculationSetup;
import org.openlca.core.math.SystemCalculator;
import org.openlca.core.math.data_quality.DQCalculationSetup;
import org.openlca.core.math.data_quality.DQResult;
import org.openlca.core.model.Exchange;
import org.openlca.core.model.Flow;
import org.openlca.core.model.ModelType;
import org.openlca.core.model.ProductSystem;
import org.openlca.core.results.ContributionResult;
import org.openlca.core.results.FullResult;
import org.openlca.core.results.SimpleResult;
import org.openlca.geo.parameter.ParameterSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wizard for setting calculation properties and run the calculation of a
 * product system
 */
public class CalculationWizard extends Wizard {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	ProductSystem productSystem;

	private CalculationWizardPage page;
	private DQSettingsPage dqPage;

	public CalculationWizard(ProductSystem productSystem) {
		this.productSystem = productSystem;
		setNeedsProgressMonitor(true);
		setWindowTitle(M.CalculationProperties);
	}

	public static void open(ProductSystem system) {
		if (system == null)
			return;
		boolean doContinue = checkForUnsavedContent(system);
		if (!doContinue)
			return;
		CalculationWizard wizard = new CalculationWizard(system);
		WizardDialog dialog = new WizardDialog(UI.shell(), wizard);
		dialog.open();
	}

	private static boolean checkForUnsavedContent(ProductSystem system) {
		IEditorPart[] dirty = Editors.getActivePage().getDirtyEditors();
		if (dirty.length == 0)
			return true;
		List<IEditorPart> relevant = new ArrayList<>();
		for (IEditorPart part : dirty) {
			if (!(part.getEditorInput() instanceof ModelEditorInput))
				continue;
			ModelEditorInput input = (ModelEditorInput) part.getEditorInput();
			ModelType type = input.getDescriptor().type;
			if (type == ModelType.PROJECT || type == ModelType.ACTOR || type == ModelType.SOURCE)
				continue;
			if (type == ModelType.PRODUCT_SYSTEM && input.getDescriptor().id != system.id)
				continue;
			relevant.add(part);
		}
		if (relevant.isEmpty())
			return true;
		int answer = Question.askWithCancel(M.UnsavedChanges, M.SomeElementsAreNotSaved);
		if (answer == IDialogConstants.NO_ID)
			return true;
		if (answer == IDialogConstants.CANCEL_ID)
			return false;
		for (IEditorPart part : relevant) {
			Editors.getActivePage().saveEditor(part, false);
		}
		return true;
	}

	@Override
	public void addPages() {
		page = new CalculationWizardPage();
		addPage(page);
		dqPage = new DQSettingsPage();
		addPage(dqPage);
	}

	@Override
	public boolean performFinish() {
		CalculationSetup setup = page.getSetup(productSystem);
		DQCalculationSetup dqSetup = null;
		if (page.doDqAssessment())
			dqSetup = dqPage.getSetup(productSystem);
		boolean storeInventoryResult = page.doStoreInventoryResult();
		saveDefaults(setup, dqSetup);
		try {
			Calculation calculation = new Calculation(
					setup, dqSetup, storeInventoryResult);
			getContainer().run(true, true, calculation);
			if (calculation.outOfMemory)
				MemoryError.show();
			return !calculation.outOfMemory;
		} catch (Exception e) {
			log.error("Calculation failed", e);
			return false;
		}
	}

	private ResultEditorInput getEditorInput(Object result,
			CalculationSetup setup, ParameterSet parameterSet,
			DQResult dqResult) {
		ResultEditorInput input = ResultEditorInput.create(setup, result)
				.with(dqResult).with(parameterSet);
		return input;
	}

	private class Calculation implements IRunnableWithProgress {

		private CalculationSetup setup;
		private DQCalculationSetup dqSetup;
		private boolean storeInventoryResult;
		private boolean outOfMemory;

		public Calculation(CalculationSetup setup, DQCalculationSetup dqSetup,
				boolean storeInventoryResult) {
			this.setup = setup;
			this.dqSetup = dqSetup;
			this.storeInventoryResult = storeInventoryResult;
		}

		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
			outOfMemory = false;
			monitor.beginTask(M.RunCalculation, IProgressMonitor.UNKNOWN);
			int size = productSystem.processes.size();
			log.trace("calculate a {} x {} system", size, size);
			try {
				switch (setup.type) {
				case UPSTREAM_ANALYSIS:
					analyse();
					break;
				case MONTE_CARLO_SIMULATION:
					SimulationEditor.open(setup);
					break;
				case CONTRIBUTION_ANALYSIS:
					solve();
					break;
				default:
					break;
				}
			} catch (OutOfMemoryError e) {
				outOfMemory = true;
			}
			monitor.done();
		}

		private void analyse() {
			log.trace("run analysis");
			SystemCalculator calculator = new SystemCalculator(
					Database.get(), App.getSolver());
			FullResult result = calculator.calculateFull(setup);
			log.trace("calculation done");
			saveInventory(result);
			DQResult dqResult = DQResult.calculate(
					Database.get(), result, dqSetup);
			Sort.sort(result);
			ResultEditorInput input = getEditorInput(
					result, setup, null, dqResult);
			Editors.open(input, AnalyzeEditor.ID);
		}

		private void solve() {
			log.trace("run quick calculation");
			SystemCalculator calc = new SystemCalculator(
					Database.get(), App.getSolver());
			ContributionResult r = calc.calculateContributions(setup);
			log.trace("calculation done");
			saveInventory(r);
			DQResult dqResult = DQResult.calculate(
					Database.get(), r, dqSetup);
			Sort.sort(r);
			ResultEditorInput input = getEditorInput(
					r, setup, null, dqResult);
			Editors.open(input, QuickResultEditor.ID);
		}

		private void saveInventory(SimpleResult r) {
			if (!storeInventoryResult)
				return;

			productSystem.inventory.clear();
			IDatabase db = Database.get();
			ProductSystemDao sysDao = new ProductSystemDao(db);
			if (r.flowIndex == null || r.flowIndex.isEmpty()) {
				productSystem = sysDao.update(productSystem);
				return;
			}

			// load the used flows
			Set<Long> flowIDs = new HashSet<>();
			r.flowIndex.each((i, f) -> {
				if (f.flow == null)
					return;
				flowIDs.add(f.flow.id);
			});
			Map<Long, Flow> flows = new FlowDao(db)
					.getForIds(flowIDs).stream()
					.collect(Collectors.toMap(f -> f.id, f -> f));

			// create the exchanges
			r.flowIndex.each((i, f) -> {
				if (f.flow == null)
					return;
				Flow flow = flows.get(f.flow.id);
				if (flow == null)
					return;
				Exchange e = Exchange.from(flow);
				e.amount = r.getTotalFlowResult(f);
				e.isInput = f.isInput;
				productSystem.inventory.add(e);
			});

			productSystem = sysDao.update(productSystem);
		}
	}
}
