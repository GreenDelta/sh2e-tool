package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class WizCradleToGatePage extends WizardPage {

	private Option selected;

	WizCradleToGatePage() {
		super("WizCradleToGatePage");
		setTitle(Scope.CRADLE_TO_GATE.label());
		setPageComplete(false);
	}

	Option cradleToGate() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);
		OptionGroup.of(
						Scope.CRADLE_TO_GATE,
						"Please state the system boundary of " +
								"the hydrogen production:")
				.renderOn(body)
				.onSelect(option -> {
					setPageComplete(true);
					selected = option;
				});
	}
}
