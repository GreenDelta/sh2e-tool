package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class WizComparativePage extends WizardPage {

	private Option selected;

	WizComparativePage() {
		super("WizComparativePage");
		setTitle("Comparative LCA");
		setPageComplete(false);
	}

	Option comparative() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var group = OptionGroup.of(
				Scope.COMPARATIVE_LCA.label(),
				"Is this a comparative LCA?",
				Option.Yes, Option.No);
		group.renderOn(body).onSelect(opt -> {
			setPageComplete(true);
			selected = opt.equals(Option.Yes)
					? Sh2e.ComparativeLCA.COMPARATIVE
					: Sh2e.ComparativeLCA.ABSOLUTE;
		});
	}

}
