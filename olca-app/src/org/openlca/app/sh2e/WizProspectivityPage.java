package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Prospectivity;
import org.openlca.app.util.UI;

class WizProspectivityPage extends WizardPage {

	private Option selected;

	WizProspectivityPage() {
		super("WizProspectivityPage");
		setTitle("Prospectivity");
		setPageComplete(false);
	}

	Option prospectivity() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var group = OptionGroup.of(
				"Prospectivity",
				"Is the technology modelled at early stage of development?",
				Option.Yes, Option.No);
		group.renderOn(body).onSelect(opt -> {
			setPageComplete(true);
			selected = opt.equals(Option.Yes)
					? Prospectivity.PROSPECTIVE
					: Prospectivity.RETROSPECTIVE;
		});
	}
}
