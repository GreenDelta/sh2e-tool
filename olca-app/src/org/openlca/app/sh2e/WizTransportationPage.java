package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.UI;


class WizTransportationPage extends WizDoubleParamPage {

	private Option lifetime;
	private Option consumption;
	private Option unit;
	private OptionGroup threeUnitGroup;
	private OptionGroup twoUnitsGroup;

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


	public IWizardPage with(Option boundaries, Option css) {
		threeUnitGroup.widget().setVisible(boundaries == Sh2e.Boundaries.USE);
		twoUnitsGroup.widget().setVisible(
				boundaries == Sh2e.Boundaries.NONE &&
						css == Sh2e.CSS.WITHOUT_CSS);
		if (boundaries == Sh2e.Boundaries.NONE && css == Sh2e.CSS.WITH_CSS) {
			unit = Sh2e.FunctionalUnit.KM;
		}

		return this;
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
		threeUnitGroup = OptionGroup.of(
				Sh2e.Scope.FUNCTIONAL_UNIT.label(),
				"Please select the functional unit:",
				Sh2e.FunctionalUnit.KM,
				Sh2e.FunctionalUnit.PASSENGER_LOAD,
				Sh2e.FunctionalUnit.FREIGHT_LOAD);
		threeUnitGroup.renderOn(body).onSelect(option -> {
			unit = option;
			checkState.run();
		});

		twoUnitsGroup = OptionGroup.of(
				Sh2e.Scope.FUNCTIONAL_UNIT.label(),
				"Please select the functional unit:",
				Sh2e.FunctionalUnit.KM,
				Sh2e.FunctionalUnit.PASSENGER_LOAD);
		twoUnitsGroup.renderOn(body).onSelect(option -> {
			unit = option;
			checkState.run();
		});
	}

}
