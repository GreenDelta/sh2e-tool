package org.openlca.app.sh2e;

import org.openlca.app.sh2e.Sh2e.Boundaries;

enum Template {

	T_1("Case 1 Cradle-to-gate 1 (hydrogen production) kg", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 1 (hydrogen production) kg.zip"),
	T_2("Case 1 Cradle-to-gate 1 (hydrogen production) kg with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 1 (hydrogen production) kg with CCS.zip"),
	T_3("Case 1 Cradle-to-gate 1 (hydrogen production) MJ", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 1 (hydrogen production) MJ.zip"),
	T_4("Case 1 Cradle-to-gate 1 (hydrogen production) MJ with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 1 (hydrogen production) MJ with CCS.zip"),
	T_5("Case 1 Cradle-to-gate 2 (hydrogen purification) kg", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 2 (hydrogen purification) kg.zip"),
	T_6("Case 1 Cradle-to-gate 2 (hydrogen purification) kg with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 2 (hydrogen purification) kg with CCS.zip"),
	T_7("Case 1 Cradle-to-gate 2 (hydrogen purification) MJ", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 2 (hydrogen purification) MJ.zip"),
	T_8("Case 1 Cradle-to-gate 3 (hydrogen compression) MJ", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 3 (hydrogen compression) MJ.zip"),
	T_9("Case 1 Cradle-to-gate 3 (hydrogen compression) MJ with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 3 (hydrogen compression) MJ with CCS.zip"),
	T_10("Case 1 Cradle-to-gate 4 (hydrogen transportation) kg", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 4 (hydrogen transportation) kg.zip"),
	T_11("Case 1 Cradle-to-gate 4 (hydrogen transportation) kg with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 4 (hydrogen transportation) kg with CCS.zip"),
	T_12("Case 1 Cradle-to-gate 4 (hydrogen transportation) MJ", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 4 (hydrogen transportation) MJ.zip"),
	T_13("Case 1 Cradle-to-gate 4 (hydrogen transportation) MJ with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 4 (hydrogen transportation) MJ with CCS.zip"),
	T_14("Case 1 Cradle-to-gate 5 (hydrogen storage) kg", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 5 (hydrogen storage) kg.zip"),
	T_15("Case 1 Cradle-to-gate 5 (hydrogen storage) kg with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 5 (hydrogen storage) kg with CCS.zip"),
	T_16("Case 1 Cradle-to-gate 5 (hydrogen storage) MJ", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 5 (hydrogen storage) MJ.zip"),
	T_17("Case 1 Cradle-to-gate 5 (hydrogen storage) MJ with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 5 (hydrogen storage) MJ with CCS.zip"),
	T_18("Case 1 Cradle-to-gate 6 (hydrogen distribution) kg", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 6 (hydrogen distribution) kg.zip"),
	T_19("Case 1 Cradle-to-gate 6 (hydrogen distribution) kg with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 6 (hydrogen distribution) kg with CCS.zip"),
	T_20("Case 1 Cradle-to-gate 6 (hydrogen distribution) MJ", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 6 (hydrogen distribution) MJ.zip"),
	T_21("Case 1 Cradle-to-gate 6 (hydrogen distribution) MJ with CCS", Boundaries.PRODUCTION, "templates/Case 1 Cradle-to-gate 6 (hydrogen distribution) MJ with CCS.zip"),

	T_22("Case 2 Hydrogen use for cogeneration (MJ)", Boundaries.USE, "templates/Case 2 Hydrogen use for cogeneration (MJ).zip"),
	T_23("Case 2 Hydrogen use for electricity generation (MJ)", Boundaries.USE, "templates/Case 2 Hydrogen use for electricity generation (MJ).zip"),
	T_24("Case 2 Hydrogen use for fuel or chemical production (kg)", Boundaries.USE, "templates/Case 2 Hydrogen use for fuel or chemical production (kg).zip"),
	T_25("Case 2 Hydrogen use for fuel production (MJ)", Boundaries.USE, "templates/Case 2 Hydrogen use for fuel production (MJ).zip"),
	T_26("Case 2 Hydrogen use for transportation (km)", Boundaries.USE, "templates/Case 2 Hydrogen use for transportation (km).zip"),
	T_27("Case 2 Hydrogen use for transportation (p.km)", Boundaries.USE, "templates/Case 2 Hydrogen use for transportation (p.km).zip"),
	T_28("Case 2 Hydrogen use for transportation (t.km)", Boundaries.USE, "templates/Case 2 Hydrogen use for transportation (t.km).zip"),

	T_29("Case 3 Hydrogen production and use for transportation (km)", Boundaries.NONE, "templates/Case 3 Hydrogen production and use for transportation (km).zip"),
	T_30("Case 3 Hydrogen production and use for transportation (p.km)", Boundaries.NONE, "templates/Case 3 Hydrogen production and use for transportation (p.km).zip"),
	T_31("Case 3 Hydrogen production with CCS and use for transportation (km)", Boundaries.NONE, "templates/Case 3 Hydrogen production with CCS and use for transportation (km).zip"),

	T_32("CCS", Boundaries.NONE, "templates/CCS.zip");

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
