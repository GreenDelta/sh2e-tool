package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.UI;

class WizStartPage extends WizardPage {

	private Option decisionOption;

	WizStartPage() {
		super("StartPage");
		setTitle("Welcome to the SH2E tool");
		setDescription("To get through this dialog, " +
				"it is recommended that you refer to the " +
				"SH2E guidelines for clarification.");
		setPageComplete(false);
	}

	Option isDecisionSupport() {
		return decisionOption;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var group = OptionGroup.of(
				"Intended application",
				"Is the application of the LCA " +
						"intended for decision support?",
				Option.Yes, Option.No
		);
		group.renderOn(body).onSelect(option -> {
			setPageComplete(true);
			decisionOption = option;
		});
	}
}
