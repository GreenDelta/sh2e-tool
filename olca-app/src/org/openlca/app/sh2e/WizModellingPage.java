package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Application;
import org.openlca.app.sh2e.Sh2e.Modelling;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.UI;

class WizModellingPage extends WizardPage {

	private Option scaleOption;
	private Option modellingOption;

	WizModellingPage() {
		super("WizModellingPage");
		setTitle("Modelling principles");
		setPageComplete(false);
	}

	Option application() {
		return scaleOption;
	}

	Option modelling() {
		return modellingOption;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		Runnable checkState = () -> setPageComplete(
				scaleOption != null && modellingOption != null);

		var scaleGroup = OptionGroup.of(
				"Scale",
				"What is the scale of the decision support?",
				Application.MICRO, Application.MACRO);
		scaleGroup.renderOn(body).onSelect(option -> {
			scaleOption = option;
			checkState.run();
		});

		var statusGroup = OptionGroup.of(
				"Status quo",
				"Will the status quo change and can " +
						"this change be modelled with a net benefit?",
				Option.Yes, Option.No);
		statusGroup.renderOn(body).onSelect(option -> {
			modellingOption = option.equals(Option.Yes)
					? Modelling.CONSEQUENTIAL
					: Modelling.ATTRIBUTIONAL;
			checkState.run();
		});
	}
}
