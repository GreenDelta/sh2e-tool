package org.openlca.app.sh2e;

import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.openlca.app.util.UI;

class TemplateDialog extends FormDialog {

	private TemplateDialog() {
		super(UI.shell());
		setBlockOnOpen(true);
	}

	static void show() {
		new TemplateDialog().open();
	}

	@Override
	protected void createFormContent(IManagedForm mForm) {
		var body = UI.header(mForm, "Create a product system");

	}
}
