package org.openlca.app.sh2e.params;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlca.app.App;
import org.openlca.app.M;
import org.openlca.app.db.Cache;
import org.openlca.app.editors.Editors;
import org.openlca.app.editors.SimpleEditorInput;
import org.openlca.app.editors.SimpleFormEditor;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.rcp.images.Images;
import org.openlca.app.util.Controls;
import org.openlca.app.util.Labels;
import org.openlca.app.util.UI;
import org.openlca.app.viewers.Viewers;
import org.openlca.core.model.RootEntity;
import org.openlca.core.model.descriptors.ImpactDescriptor;

public class ParameterAnalysisResultPage extends SimpleFormEditor {

	private ParamResult result;

	static void open(ParamResult result) {
		if (result == null)
			return;
		var key = Cache.getAppCache().put(result);
		var input = new SimpleEditorInput(key, "Parameter analysis results");
		Editors.open(input, "ParameterAnalysisResultPage");
	}

	@Override
	public void init(
			IEditorSite site, IEditorInput input
	) throws PartInitException {
		super.init(site, input);
		setTitleImage(Icon.ANALYSIS_RESULT.get());
		var inp = (SimpleEditorInput) input;
		result = Cache.getAppCache().remove(inp.id, ParamResult.class);
	}

	@Override
	protected FormPage getPage() {
		return new Page();
	}

	private class Page extends FormPage {

		Page() {
			super(ParameterAnalysisResultPage.this, "ParamResultPage", "Results");
		}

		@Override
		protected void createFormContent(IManagedForm mForm) {
			var form = UI.header(mForm, "Parameter analysis results");
			var tk = mForm.getToolkit();
			var body = UI.body(form, tk);
			setupSection(body, tk);
			chartSection(body, tk);
			form.reflow(true);
		}

		private void setupSection(Composite body, FormToolkit tk) {
			var comp = UI.formSection(body, tk, "Calculation setup");
			link(comp, tk, M.ProductSystem, result.system());
			link(comp, tk, M.ImpactAssessmentMethod, result.method());
			var allocation = UI.labeledText(comp, tk, M.AllocationMethod);
			allocation.setText(Labels.of(result.allocation()));
			allocation.setEditable(false);
			var iterations = UI.labeledText(comp, tk, "Number of iterations");
			iterations.setText(Integer.toString(result.count()));
		}

		private void link(
				Composite comp, FormToolkit tk, String label, RootEntity e
		) {
			UI.label(comp, tk, label);
			var link = UI.imageHyperlink(comp, tk, SWT.TOP);
			link.setText(Labels.name(e));
			link.setImage(Images.get(e));
			Controls.onClick(link, $ -> App.open(e));
		}

		private void chartSection(Composite body, FormToolkit tk) {
			var comp = UI.formSection(body, tk, "Impact assessment results");
			UI.gridLayout(comp, 1);
			var top = tk.createComposite(comp);
			UI.fillHorizontal(top);
			UI.gridLayout(top, 2);
			UI.label(top, tk, M.ImpactCategory);
			var impacts = result.impacts();
			var impactCombo = DescriptorCombo.of(top, tk, impacts);
			impactCombo.addSelectionChangedListener(e -> {
				ImpactDescriptor d = Viewers.getFirstSelected(impactCombo);
				System.out.println(d.name);
			});
		}
	}
}
