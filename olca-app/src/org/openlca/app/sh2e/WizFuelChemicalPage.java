package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openlca.app.util.UI;

public class WizFuelChemicalPage extends WizDoubleParamPage {

	private Sh2e.Option purity;
	private Sh2e.Option pressure;
	private Sh2e.Option temperature;
	private Sh2e.Option netCalorific;
	private Sh2e.Option unit;

	WizFuelChemicalPage() {
		super("WizFuelChemicalPage");
		setTitle("Fuel/Chemical production");
		setPageComplete(false);

		checkState = () -> setPageComplete(
				purity != null
						&& pressure != null
						&& temperature != null
						&& unit != null);
	}

	Sh2e.Option purity() {
		return purity;
	}

	Sh2e.Option pressure() {
		return pressure;
	}

	Sh2e.Option temperature() {
		return temperature;
	}

	Sh2e.Option netCalorific() {
		return netCalorific;
	}

	Sh2e.Option unit() {
		return unit;
	}


	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		Group param = new Group(body, SWT.NONE);
		UI.fillHorizontal(param);
		param.setText("Fuel/Chemical production parameters");
		UI.gridLayout(param, 1);
		labeledText(param, Sh2e.Scope.FUEL_PURITY, o -> purity = o);
		labeledText(param, Sh2e.Scope.FUEL_PRESSURE, o -> pressure = o);
		labeledText(param, Sh2e.Scope.FUEL_TEMPERATURE, o -> temperature = o);
		labeledText(param, Sh2e.Scope.FUEL_CALORIFIC, o -> netCalorific = o);

		OptionGroup unitGroup = OptionGroup.of(
				Sh2e.Scope.FUNCTIONAL_UNIT.label(),
				"Please select the functional unit:",
				Sh2e.FunctionalUnit.KG_FUEL_CHEMICAL,
				Sh2e.FunctionalUnit.MJ_FUEL);
		unitGroup.renderOn(body).onSelect(option -> {
			unit = option;
			checkState.run();
		});
	}

}
