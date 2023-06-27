package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.util.UI;

class WizTemplatePage extends WizardPage {

	private final TemplateWizard wizard;

	WizTemplatePage(TemplateWizard wizard) {
		super("WizTemplatePage");
		this.wizard = wizard;
		setTitle("Select a template");
		setPageComplete(false);
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		// todo
	}
}
