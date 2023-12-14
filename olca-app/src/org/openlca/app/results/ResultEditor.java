package org.openlca.app.results;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.openlca.app.M;
import org.openlca.app.db.Cache;
import org.openlca.app.editors.Editors;
import org.openlca.app.preferences.FeatureFlag;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.results.analysis.sankey.SankeyEditor;
import org.openlca.app.results.contributions.ContributionTreePage;
import org.openlca.app.results.contributions.ProcessResultPage;
import org.openlca.app.results.contributions.TagResultPage;
import org.openlca.app.results.contributions.locations.LocationPage;
import org.openlca.app.results.grouping.GroupPage;
import org.openlca.app.results.impacts.ImpactTreePage;
import org.openlca.app.sh2e.slca.SocialRiskResult;
import org.openlca.app.sh2e.slca.ui.SocialResultPage;
import org.openlca.app.util.ErrorReporter;
import org.openlca.app.util.Labels;
import org.openlca.app.util.MemoryError;
import org.openlca.core.math.data_quality.DQResult;
import org.openlca.core.model.CalculationSetup;
import org.openlca.core.results.LcaResult;
import org.openlca.core.results.ResultItemOrder;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.openlca.app.tools.graphics.EditorActionBarContributor.refreshActionBar;

/**
 * View for the analysis results of a product system.
 */
public class ResultEditor extends FormEditor {

	public static final String ID = "editors.analyze";

	public LcaResult result;
	public CalculationSetup setup;
	public DQResult dqResult;
	public ResultItemOrder items;
	private SocialRiskResult socialResult;

	public static void open(
			CalculationSetup setup,
			LcaResult result,
			DQResult dqResult,
			SocialRiskResult socialResult) {
		var input = ResultEditorInput
				.create(setup, result)
				.with(dqResult)
				.with(socialResult);
		Editors.open(input, ResultEditor.ID);
	}

	@Override
	public void init(IEditorSite site, IEditorInput iInput)
			throws PartInitException {
		super.init(site, iInput);
		setTitleImage(Icon.ANALYSIS_RESULT.get());
		var inp = (ResultEditorInput) iInput;
		result = Cache.getAppCache().remove(inp.resultKey, LcaResult.class);
		if (inp.dqResultKey != null) {
			dqResult = Cache.getAppCache().remove(inp.dqResultKey, DQResult.class);
		}
		if (inp.socialResultKey != null) {
			socialResult = Cache.getAppCache()
					.remove(inp.socialResultKey, SocialRiskResult.class);
		}
		setup = Cache.getAppCache().remove(inp.setupKey, CalculationSetup.class);
		items = ResultItemOrder.of(result);
		Sort.sort(items);
		setPartName(M.ResultsOf + ": " + Labels.name(setup.target()));
	}

	@Override
	public void dispose() {
		result.dispose();
		super.dispose();
	}

	@Override
	protected void addPages() {
		try {
			addPage(new InfoPage(this));
			addPage(new InventoryPage(this));
			if (result.hasImpacts())
				addPage(new ImpactTreePage(this));
			if (result.hasImpacts() && setup.nwSet() != null)
				addPage(new NwResultPage(this));
			if (socialResult != null) {
				addPage(new SocialResultPage(this, socialResult));
			}
			addPage(new ProcessResultPage(this));
			addPage(new ContributionTreePage(this));
			addPage(new GroupPage(this));
			addPage(new LocationPage(this));

			var sankeyEditor = new SankeyEditor(this);
			var sankeyEditorIndex = addPage(sankeyEditor, getEditorInput());
			setPageText(sankeyEditorIndex, M.SankeyDiagram);
			// Add a page listener to set the graph when it is activated the first
			// time.
			setSankeyPageListener(sankeyEditor);

			if (result.hasImpacts()) {
				addPage(new ImpactChecksPage(this));
			}
			if (FeatureFlag.TAG_RESULTS.isEnabled()) {
				addPage(new TagResultPage(this));
			}

		} catch (Throwable e) {
			ErrorReporter.on("failed to create result pages", e);
			this.close(false);
		}
	}

	@Override
	protected void pageChange(int newPageIndex) {
		try {
			super.pageChange(newPageIndex);
		} catch (OutOfMemoryError e) {
			MemoryError.show();
			this.close(false);
		}
	}

	private void setSankeyPageListener(SankeyEditor sankeyEditor) {
		var sankeyInit = new AtomicReference<IPageChangedListener>();
		IPageChangedListener fn = e -> {
			if (!Objects.equals(e.getSelectedPage(), sankeyEditor))
				return;
			var listener = sankeyInit.get();
			if (listener == null)
				return;

			sankeyEditor.onFirstActivation();

			// Artificially refreshing the ActionBarContributor.
			refreshActionBar(this);

			removePageChangedListener(listener);
			sankeyInit.set(null);
		};
		sankeyInit.set(fn);
		addPageChangedListener(fn);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
		SaveResultDialog.open(this);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void setFocus() {
	}

	static class ResultEditorInput implements IEditorInput {

		private final String name;
		private final String resultKey;
		private final String setupKey;
		private String dqResultKey;
		private String socialResultKey;

		private ResultEditorInput(
				String name, String resultKey, String setupKey) {
			this.name = name;
			this.resultKey = resultKey;
			this.setupKey = setupKey;
		}

		static ResultEditorInput create(CalculationSetup setup, LcaResult result) {
			if (setup == null)
				return null;
			var name = Labels.name(setup.target());
			var resultKey = Cache.getAppCache().put(result);
			var setupKey = Cache.getAppCache().put(setup);
			return new ResultEditorInput(name, resultKey, setupKey);
		}

		public ResultEditorInput with(DQResult dqResult) {
			if (dqResult != null) {
				dqResultKey = Cache.getAppCache().put(dqResult);
			}
			return this;
		}

		public ResultEditorInput with(SocialRiskResult socialResult) {
			if (socialResult != null) {
				socialResultKey = Cache.getAppCache().put(socialResult);
			}
			return this;
		}

		@Override
		@SuppressWarnings({"unchecked", "rawtypes"})
		public Object getAdapter(Class adapter) {
			return null;
		}

		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public ImageDescriptor getImageDescriptor() {
			return Icon.CHART.descriptor();
		}

		@Override
		public String getName() {
			return M.Results + ": " + name;
		}

		@Override
		public IPersistableElement getPersistable() {
			return null;
		}

		@Override
		public String getToolTipText() {
			return getName();
		}
	}
}
