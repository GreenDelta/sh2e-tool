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
	private Option statusOption;
	private Option netOption;

	private OptionGroup netBenefit;

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

		Runnable checkState = () -> {
			if (netBenefit != null && netBenefit.widget() != null) {
					netBenefit.widget().setVisible(Option.Yes.equals(statusOption));
			}

			if ((scaleOption == null || statusOption == null)
					|| (statusOption.equals(Option.Yes) && netOption == null)) {
				setPageComplete(false);
				return;
			}

			modellingOption = Option.Yes.equals(statusOption)
					&& Option.Yes.equals(netOption)
					? Modelling.CONSEQUENTIAL
					: Modelling.ATTRIBUTIONAL;
			setPageComplete(true);
		};

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
				"Will the status quo change?",
				Option.Yes, Option.No);
		statusGroup.renderOn(body).onSelect(option -> {
			statusOption = option;
			checkState.run();
		});

		netBenefit = OptionGroup.of(
				"Net benefit",
				"Can this change be modelled with a net benefit?",
				Option.Yes, Option.No);
		netBenefit.renderOn(body).onSelect(option -> {
			netOption = option;
			checkState.run();
		});
		netBenefit.widget().setVisible(false);
	}

}
