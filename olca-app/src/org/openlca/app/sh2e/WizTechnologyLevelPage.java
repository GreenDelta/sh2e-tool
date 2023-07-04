package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class WizTechnologyLevelPage extends WizardPage {

	private Option selected;

	WizTechnologyLevelPage() {
		super("WizTechnologyLevelPage");
		setTitle(Scope.TECHNOLOGY_LEVEL.label());
		setPageComplete(false);
	}

	Option technologyLevel() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);
		OptionGroup.of(
						Scope.TECHNOLOGY_LEVEL,
						"Please state the Technology Readiness Level (TRL)" +
								" of the involved technology:")
				.renderOn(body)
				.onSelect(option -> {
					setPageComplete(true);
					selected = option;
				});
	}
}
