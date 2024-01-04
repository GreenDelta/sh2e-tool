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
import org.openlca.core.model.RootEntity;
import org.openlca.core.model.descriptors.RootDescriptor;

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

			var comp = UI.formSection(body, tk, "Calculation setup");
			link(comp, tk, M.ProductSystem, result.system());
			link(comp, tk, M.ImpactAssessmentMethod, result.method());
			var allocation = UI.labeledText(comp, tk, M.AllocationMethod);
			allocation.setText(Labels.of(result.allocation()));
			allocation.setEditable(false);
			var iterations = UI.labeledText(comp, tk, "Number of iterations");
			iterations.setText(Integer.toString(result.count()));

			form.reflow(true);
		}

		private void link(
				Composite comp, FormToolkit tk, String label, Object entity
		) {
			UI.label(comp, tk, label);
			var link = UI.imageHyperlink(comp, tk, SWT.TOP);
			if (entity instanceof RootDescriptor d) {
				link.setText(Labels.name(d));
				link.setImage(Images.get(d));
				Controls.onClick(link, e -> App.open(d));
			} else if (entity instanceof RootEntity ce) {
				link.setText(Labels.name(ce));
				link.setImage(Images.get(ce));
				Controls.onClick(link, e -> App.open(ce));
			}
		}
	}
}
