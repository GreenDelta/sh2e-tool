package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class WizProductionPurposePage extends WizardPage {

	private Option selected;

	WizProductionPurposePage() {
		super("WizCradleToGatePage");
		setTitle(Scope.PRODUCTION_PURPOSE.label());
		setPageComplete(false);
	}

	Option purpose() {
		return selected;
	}

	public void setPurpose(Sh2e.ProductionPurpose purpose) {
		selected = purpose;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);
		OptionGroup.of(
						Scope.PRODUCTION_PURPOSE,
						"Please state the system boundary of " +
								"the hydrogen production:",
						Sh2e.ProductionPurpose.NONE)
				.renderOn(body)
				.onSelect(option -> {
					setPageComplete(true);
					selected = option;
				});
	}

}
