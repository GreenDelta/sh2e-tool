package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class WizBoundariesPage extends WizardPage {

	private Option selected;

	WizBoundariesPage() {
		super("WizBoundariesPage");
		setTitle("System boundaries");
		setPageComplete(false);
	}

	Option selected() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var group = OptionGroup.of(
				Scope.BOUNDARIES,
				"Please select the system boundaries of "
						+ "the hydrogen system to be modelled");
		group.renderOn(body).onSelect(opt -> {
			setPageComplete(true);
			selected = opt;
		});
	}
}
