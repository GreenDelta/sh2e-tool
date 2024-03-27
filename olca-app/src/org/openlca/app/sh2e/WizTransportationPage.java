package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.UI;


class WizTransportationPage extends WizDoubleParamPage {

	private Option lifetime;
	private Option consumption;
	private Option unit;
	private OptionGroup unitGroup;

	WizTransportationPage() {
		super("WizTransportationPage");
		setTitle("Transportation Parameters");
		setPageComplete(false);

		checkState = () -> setPageComplete(
				lifetime != null && consumption != null && unit != null);
	}

	Option lifetime() {
		return lifetime;
	}

	Option consumption() {
		return consumption;
	}

	Option unit() {
		return unit;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var param = new Group(body, SWT.NONE);
		UI.fillHorizontal(param);
		param.setText("Vehicle parameters");
		UI.gridLayout(param, 1);
		labeledText(param, Sh2e.Scope.VEHICLE_LIFETIME, o -> lifetime = o);
		labeledText(param, Sh2e.Scope.VEHICLE_COMSUMPTION, o -> consumption = o);

		// Depending on context, the user can select between 3, 2 or 1 units.
		unitGroup = OptionGroup.of(
				Sh2e.Scope.FUNCTIONAL_UNIT.label(),
				"Please select the functional unit:",
				Sh2e.FunctionalUnit.KM,
				Sh2e.FunctionalUnit.PASSENGER_LOAD,
				Sh2e.FunctionalUnit.FREIGHT_LOAD);
		unitGroup.renderOn(body).onSelect(option -> {
			unit = option;
			checkState.run();
		});
	}

}
