package org.openlca.app.sh2e;

import org.openlca.app.sh2e.Sh2e.Boundaries;

enum Template {

	T_1("1 - System producing hydrogen", Boundaries.PRODUCTION, null),
	T_2("2.1 - System producing hydrogen and by-product/s - system expansion", Boundaries.PRODUCTION, null),
	T_3("2.2 - System producing hydrogen and by-product/s - physical allocation", Boundaries.PRODUCTION, null),
	T_4("2.3 - System producing hydrogen and by-product/s - economic allocation", Boundaries.PRODUCTION, null),
	T_5("3.1 - System producing hydrogen as by-product - system expansion", Boundaries.PRODUCTION, null),
	T_6("3.2 - System producing hydrogen as by-product - physical allocation", Boundaries.PRODUCTION, null),
	T_7("3.3 - System producing hydrogen as by-product - economic allocation", Boundaries.PRODUCTION, null),
	T_8("4.1 - System producing hydrogen with CCS", Boundaries.PRODUCTION, null),
	T_9("4.2 - System producing hydrogen with CCU", Boundaries.PRODUCTION, null),
	T_10("5.1 - System producing hydrogen from biomass", Boundaries.PRODUCTION, null),
	T_11("5.2 - System producing hydrogen from biomass with CCS", Boundaries.PRODUCTION, null),
	T_12("5.3 - System producing hydrogen from biomass with CCU", Boundaries.PRODUCTION, null),
	T_13("6 - System producing hydrogen, incl. distribution", Boundaries.PRODUCTION, null),

	T_14("7 - System using hydrogen (generic)", Boundaries.USE, null),
	T_15("7.1 - System using hydrogen - for transportation", Boundaries.USE, "x_template_7.1.zip"),
	T_16("7.2 - System using hydrogen - for fuels and chemicals", Boundaries.USE, null),
	T_17("7.3 - System using hydrogen - for electricity generation", Boundaries.USE, null),
	T_18("7.4 - System using hydrogen - case of multifunctionality (generic)", Boundaries.USE, null),
	T_19("7.4.1 - System using hydrogen - for electricity and heat generation - system expansion", Boundaries.USE, null),
	T_20("7.4.2 - System using hydrogen - for electricity and heat generation - physical allocation", Boundaries.USE, null),
	T_21("7.4.3 - System using hydrogen - for electricity and heat generation - economic allocation", Boundaries.USE, null),
	T_22("7.4.4 - System using hydrogen - for electricity and heat generation - heat as an emission", Boundaries.USE, null),
	T_23("7.5 - System using hydrogen and CO2-based product from different systems", Boundaries.USE, null),
	T_24("7.6 - System using hydrogen and CO2-based product from the same system", Boundaries.USE, null);

	private final String label;
	private final String file;
	private final Boundaries boundaries;

	Template(String label, Boundaries boundaries, String file) {
		this.label = label;
		this.boundaries = boundaries;
		this.file = file;
	}

	Boundaries boundaries () {
		return boundaries;
	}

	String file() {
		return this.file;
	}

	String label() {
		return this.label;
	}
}
