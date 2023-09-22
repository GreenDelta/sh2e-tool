package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.UI;

class WizUsePurposePage extends WizardPage {

	private Option selected;

	public final Runnable checkState = () -> setPageComplete(selected != null);

	WizUsePurposePage() {
		super("WizUsePurposePage");
		setTitle("Hydrogen use purpose");
		setPageComplete(false);
	}

	Option purpose() {
		return selected;
	}

	public void setPurpose(Sh2e.UsePurpose purpose) {
		selected = purpose;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		OptionGroup.of(
						Sh2e.Scope.USE_PURPOSE,
						"Please select the purpose of the hydrogen use:",
						Sh2e.UsePurpose.NONE)
				.renderOn(body)
				.onSelect(option -> {
					selected = option;
					checkState.run();
				});
	}
}
