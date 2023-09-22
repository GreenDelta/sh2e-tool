package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.UI;

import java.util.List;


class WizProductionPage extends WizDoubleParamPage {

	private Option netCalorific;
	private Option purity;
	private Option temperature;
	private Option pressure;
	private Option capacity;
	private Option unit;
	private Option ccs;

	private boolean withUse;
	private Group param;
	private Text capacityText;
	private OptionGroup unitGroup;


	WizProductionPage() {
		super("WizProductionPage");
		setTitle("Hydrogen Production Parameters");
		setPageComplete(false);

		checkState = () -> setPageComplete(
				netCalorific != null &&
						purity != null &&
						temperature != null &&
						pressure != null &&
						(withUse || capacity != null) &&
						(withUse || unit != null) &&
						ccs != null
		);
	}

	Option netCalorific() {
		return netCalorific;
	}

	Option purity() {
		return purity;
	}

	Option temperature() {
		return temperature;
	}

	Option pressure() {
		return pressure;
	}

	Option capacity() {
		return capacity;
	}

	Option unit() {
		return unit;
	}

	Option ccs() {
		return ccs;
	}

	public WizProductionPage withUse(boolean withUse) {
		this.withUse = withUse;

		unitGroup.widget().setVisible(!withUse);

		capacityText.setEnabled(!withUse);
		// Set the label disability
		var controls = param.getChildren();
		var index = List.of(controls).indexOf(capacityText);
		if (index < 1)
			return this;
		var capacityLabel = controls[index - 1];
		capacityLabel.setEnabled(!withUse);
		return this;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		param = new Group(body, SWT.NONE);
		UI.fillHorizontal(param);
		param.setText("Hydrogen production parameters");
		UI.gridLayout(param, 1);
		labeledText(param, Sh2e.Scope.HYDROGEN_CALORIFIC, o -> netCalorific = o);
		labeledText(param, Sh2e.Scope.HYDROGEN_PURITY, o -> purity = o);
		labeledText(param, Sh2e.Scope.HYDROGEN_PRESSURE, o -> pressure = o);
		labeledText(param, Sh2e.Scope.HYDROGEN_TEMPERATURE, o -> temperature = o);
		capacityText = labeledText(param, Sh2e.Scope.HYDROGEN_CAPACITY,
				o -> capacity = o);

		var ccsGroup = OptionGroup.of(
				Sh2e.Scope.CCS,
				"Has carbon capture and storage technology been installed in the "
				+ "hydrogen production plant?");
		ccsGroup.renderOn(body).onSelect(option -> {
			ccs = option;
			checkState.run();
		});

		unitGroup = OptionGroup.of(
				Sh2e.Scope.FUNCTIONAL_UNIT.label(),
				"Please select the functional unit:",
				Sh2e.FunctionalUnit.KG_H2,
				Sh2e.FunctionalUnit.MJ_H2);
		unitGroup.renderOn(body).onSelect(option -> {
			unit = option;
			checkState.run();
		});
	}

}
