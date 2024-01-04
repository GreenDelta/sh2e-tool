package org.openlca.app.sh2e.params;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlca.app.M;
import org.openlca.app.components.ParameterRedefDialog;
import org.openlca.app.db.Database;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.rcp.images.Images;
import org.openlca.app.util.Actions;
import org.openlca.app.util.Labels;
import org.openlca.app.util.MsgBox;
import org.openlca.app.util.UI;
import org.openlca.app.viewers.Viewers;
import org.openlca.app.viewers.tables.Tables;
import org.openlca.app.viewers.tables.modify.DoubleCellModifier;
import org.openlca.app.viewers.tables.modify.ModifySupport;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.ImpactMethod;
import org.openlca.core.model.ProductSystem;
import org.openlca.core.model.descriptors.Descriptor;
import org.openlca.util.Strings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ParameterAnalysisDialog extends FormDialog {

	private final IDatabase db = Database.get();
	private final List<Param> params = new ArrayList<>();
	private TableComboViewer systemCombo;
	private TableComboViewer methodCombo;
	private Spinner iterationSpinner;
	private TableViewer paramTable;

	public static void show() {
		if (Database.get() == null) {
			MsgBox.info(M.NoDatabaseOpened, M.NeedOpenDatabase);
			return;
		}
		new ParameterAnalysisDialog().open();
	}

	private ParameterAnalysisDialog() {
		super(UI.shell());
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Parameter analysis");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	@Override
	protected void createFormContent(IManagedForm form) {
		var tk = form.getToolkit();
		var body = UI.dialogBody(form.getForm(), tk);

		var top = tk.createComposite(body);
		UI.fillHorizontal(top);
		UI.gridLayout(top, 2);

		UI.label(top, tk, M.ProductSystem);
		systemCombo = createCombo(top, tk, db.getDescriptors(ProductSystem.class));

		UI.label(top, tk, M.ImpactAssessmentMethod);
		methodCombo = createCombo(top, tk, db.getDescriptors(ImpactMethod.class));

		UI.label(top, tk, M.NumberOfIterations);
		iterationSpinner = UI.spinner(top, tk, SWT.BORDER);
		iterationSpinner.setValues(10, 2, 100_000, 0, 1, 10);

		paramTable = Tables.createViewer(
				body, M.Parameter, M.Context, "Start value", "End value");
		paramTable.setLabelProvider(new ParamLabel());
		new ModifySupport<Param>(paramTable)
				.bind("Start value", new ValueModifier(true))
				.bind("End value", new ValueModifier(false));
		Tables.bindColumnWidths(paramTable, 0.3, 0.3, 0.2, 0.2);
		var onAdd = Actions.onAdd(this::addParams);
		var onRemove = Actions.onRemove(this::removeParam);
		Actions.bind(paramTable, onAdd, onRemove);
		paramTable.setInput(params);
	}

	private TableComboViewer createCombo(
			Composite comp, FormToolkit tk, List<? extends Descriptor> descriptors
	) {
		var combo = UI.tableCombo(comp, tk, SWT.BORDER | SWT.READ_ONLY);
		UI.fillHorizontal(combo);
		var viewer = new TableComboViewer(combo);
		viewer.setLabelProvider(new ComboLabel());
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object o1, Object o2) {
				if (!(o1 instanceof Descriptor d1) || !(o2 instanceof Descriptor d2))
					return super.compare(viewer, o1, o2);
				var n1 = Labels.name(d1);
				var n2 = Labels.name(d2);
				return Strings.compare(n1, n2);
			}
		});
		viewer.setInput(descriptors);
		if (!descriptors.isEmpty()) {
			viewer.setSelection(new StructuredSelection(descriptors.get(0)));
		}
		return viewer;
	}

	private void addParams() {
		var contexts = new HashSet<Long>(1);
		var obj = Viewers.getFirstSelected(systemCombo);
		if (obj instanceof Descriptor d) {
			contexts.add(d.id);
		}
		var redefs = ParameterRedefDialog.select(contexts);
		var added = false;
		for (var redef : redefs) {
			var param = params.stream()
					.filter(p -> p.hasRedef(redef))
					.findAny()
					.orElse(null);
			if (param != null)
				continue;
			added = true;
			param = Param.of(redef, db);
			params.add(param);
		}
		if (added) {
			paramTable.setInput(params);
		}
	}

	private void removeParam() {
		if (!(Viewers.getFirstSelected(paramTable) instanceof Param param))
			return;
		params.remove(param);
		paramTable.setInput(params);
	}

	private static class ComboLabel extends BaseLabelProvider
			implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object obj, int col) {
			return col == 0 && obj instanceof Descriptor d
					? Images.get(d)
					: null;
		}

		@Override
		public String getColumnText(Object obj, int col) {
			return col == 0 && obj instanceof Descriptor d
					? Labels.name(d)
					: null;
		}
	}

	private static class ParamLabel extends BaseLabelProvider
			implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object obj, int col) {
			if (!(obj instanceof Param param))
				return null;
			if (col == 0)
				return Icon.FORMULA.get();
			if (col == 1 && param.context != null)
				return Images.get(param.context);
			return null;
		}

		@Override
		public String getColumnText(Object obj, int col) {
			if (!(obj instanceof Param param))
				return null;
			return switch (col) {
				case 0 -> param.redef.name;
				case 1 -> param.context != null
						? Labels.name(param.context)
						: "global";
				case 2 -> Double.toString(param.start);
				case 3 -> Double.toString(param.end);
				default -> null;
			};
		}
	}

	@Override
	protected void okPressed() {

		int count = iterationSpinner.getSelection();
		if (count < 2) {
			MsgBox.info("At least 2 iterations are required",
					"For the analysis, you need to run at least 2 iterations");
			return;
		}

		if (params.isEmpty()) {
			MsgBox.info("No parameters selected",
					"You have to add at least one parameter to the analysis setup.");
			return;
		}

		var system = Viewers.getFirstSelected(systemCombo) instanceof Descriptor d
				? db.get(ProductSystem.class, d.id)
				: null;
		if (system == null) {
			MsgBox.info("No product system selected",
					"A product system is required to run a parameter analysis");
			return;
		}

		var method = Viewers.getFirstSelected(methodCombo) instanceof Descriptor m
				? db.get(ImpactMethod.class, m.id)
				: null;
		if (method == null) {
			MsgBox.info("No impact assessment method selected",
					"An impact assessment method is required to run a parameter analysis");
			return;
		}
		if (method.impactCategories.isEmpty()) {
			MsgBox.info("No impact categories in method",
					"The selected impact assessment method does not have any impact categories");
			return;
		}


	}


	private static class ValueModifier extends DoubleCellModifier<Param> {

		private final boolean forStart;

		ValueModifier(boolean forStart) {
			this.forStart = forStart;
		}

		@Override
		public Double getDouble(Param param) {
			if (param == null)
				return null;
			return forStart ? param.start : param.end;
		}

		@Override
		public void setDouble(Param param, Double val) {
			if (param == null)
				return;
			double v = val == null ? 0 : val;
			if (forStart) {
				param.start = v;
			} else {
				param.end = v;
			}
		}
	}
}
