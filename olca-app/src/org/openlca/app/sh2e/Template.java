package org.openlca.app.sh2e;

import org.openlca.app.sh2e.Sh2e.Boundaries;
import org.openlca.app.sh2e.Sh2e.CSS;
import org.openlca.app.sh2e.Sh2e.FunctionalUnit;
import org.openlca.app.sh2e.Sh2e.ProductionPurpose;
import org.openlca.app.sh2e.Sh2e.UsePurpose;

import java.util.ArrayList;
import java.util.List;

enum Template {

	P_KG("Case 1 Cradle-to-gate 1 (hydrogen production) kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	P_KG_CSS("Case 1 Cradle-to-gate 1 (hydrogen production) kg with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	P_MJ("Case 1 Cradle-to-gate 1 (hydrogen production) MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	P_MJ_CSS("Case 1 Cradle-to-gate 1 (hydrogen production) MJ with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	PU_KG("Case 1 Cradle-to-gate 2 (hydrogen purification) kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	PU_KG_CSS("Case 1 Cradle-to-gate 2 (hydrogen purification) kg with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	PU_MJ("Case 1 Cradle-to-gate 2 (hydrogen purification) MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	PU_MJ_CSS("Case 1 Cradle-to-gate 2 (hydrogen purification) MJ with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	C_KG("Case 1 Cradle-to-gate 3 (hydrogen compression) kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	C_KG_CSS("Case 1 Cradle-to-gate 3 (hydrogen compression) kg with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	C_MJ("Case 1 Cradle-to-gate 3 (hydrogen compression) MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	C_MJ_CSS("Case 1 Cradle-to-gate 3 (hydrogen compression) MJ with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	P_T_KG("Case 1 Cradle-to-gate 4 (hydrogen transportation) kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	P_T_KG_CSS("Case 1 Cradle-to-gate 4 (hydrogen transportation) kg with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	P_T_MJ("Case 1 Cradle-to-gate 4 (hydrogen transportation) MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	P_T_MJ_CSS("Case 1 Cradle-to-gate 4 (hydrogen transportation) MJ with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	S_KG("Case 1 Cradle-to-gate 5 (hydrogen storage) kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	S_KG_CSS("Case 1 Cradle-to-gate 5 (hydrogen storage) kg with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	S_MJ("Case 1 Cradle-to-gate 5 (hydrogen storage) MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	S_MJ_CSS("Case 1 Cradle-to-gate 5 (hydrogen storage) MJ with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	D_KG("Case 1 Cradle-to-gate 6 (hydrogen distribution) kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),
	D_KG_CSS("Case 1 Cradle-to-gate 6 (hydrogen distribution) kg with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),
	D_MJ("Case 1 Cradle-to-gate 6 (hydrogen distribution) MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),
	D_MJ_CSS("Case 1 Cradle-to-gate 6 (hydrogen distribution) MJ with CCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),

	CO("Case 2 Hydrogen use for cogeneration (MJ).zip",
			Boundaries.USE,
			FunctionalUnit.MJ_COGENERATION,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.HEAT),
	E("Case 2 Hydrogen use for electricity generation (MJ).zip",
			Boundaries.USE,
			FunctionalUnit.MJ_ELEC,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.ELECTRICITY),
	CH("Case 2 Hydrogen use for fuel or chemical production (kg).zip",
			Boundaries.USE,
			FunctionalUnit.KG_FUEL_CHEMICAL,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.CHEMICALS),
	F("Case 2 Hydrogen use for fuel production (MJ).zip",
			Boundaries.USE,
			FunctionalUnit.MJ_FUEL,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.FUELS),
	U_T_KM("Case 2 Hydrogen use for transportation (km).zip",
			Boundaries.USE,
			FunctionalUnit.KM,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.TRANSPORTATION),
	U_T_PKM("Case 2 Hydrogen use for transportation (p.km).zip",
			Boundaries.USE,
			FunctionalUnit.PASSENGER_LOAD,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.TRANSPORTATION),
	U_T_TKM("Case 2 Hydrogen use for transportation (t.km).zip",
			Boundaries.USE,
			FunctionalUnit.FREIGHT_LOAD,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.TRANSPORTATION),

	N_T_KM("Case 3 Hydrogen production and use for transportation (km).zip",
			Boundaries.NONE,
			FunctionalUnit.KM,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION),
	N_T_PKM("Case 3 Hydrogen production and use for transportation (p.km).zip",
			Boundaries.NONE,
			FunctionalUnit.PASSENGER_LOAD,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION),
	N_T_CSS("Case 3 Hydrogen production with CCS and use for transportation (km).zip",
			Boundaries.NONE,
			FunctionalUnit.KM,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION);

	private final String file;
	private final Boundaries boundaries;
	private final FunctionalUnit unit;
	private final CSS css;
	private final ProductionPurpose productionPurpose;
	private final UsePurpose usePurpose;

	Template(String file, Boundaries boundaries, FunctionalUnit unit, CSS css,
			ProductionPurpose productionPurpose, UsePurpose usePurpose) {
		this.file = "templates/" + file;
		this.boundaries = boundaries;
		this.unit = unit;
		this.css = css;
		this.productionPurpose = productionPurpose;
		this.usePurpose = usePurpose;
	}

	String label() {
		return labelOf(boundaries, css, unit, productionPurpose, usePurpose);
	}

	static String labelOf(Boundaries boundaries, CSS css, FunctionalUnit unit, ProductionPurpose productionPurpose, UsePurpose usePurpose) {
		String label;
		if (boundaries == Boundaries.PRODUCTION) {
			label = productionPurpose.label();
			label += " (" + unit.label() + ")";
			label += css == CSS.WITH_CSS ? " with CSS" : "";
		} else if (boundaries == Boundaries.USE) {
			label = boundaries.label() + " for ";
			label += usePurpose.label().toLowerCase();
			label += css == CSS.WITH_CSS ? " with CSS" : "";
			label += " (" + unit.label() + ")";
		} else {
			label = Boundaries.PRODUCTION.label();
			label += css == CSS.WITH_CSS ? " with CSS and use for " : " and use for ";
			label += usePurpose.label().toLowerCase();
			label += " (" + unit.label() + ")";
		}
		return label;
	}

	String file() {
		return this.file;
	}

	static class Filter {

		Boundaries boundaries;
		CSS css;
		FunctionalUnit unit;
		ProductionPurpose productionPurpose;
		UsePurpose usePurpose;

		void add(Sh2e.Option... options) {
			for (var option : options) {
				if (option instanceof Boundaries b)
					boundaries = b;
				if (option instanceof CSS c)
					css = c;
				if (option instanceof FunctionalUnit u)
					unit = u;
				if (option instanceof ProductionPurpose p)
					productionPurpose = p == ProductionPurpose.NONE ? null : p;
				if (option instanceof UsePurpose u)
					usePurpose = u == UsePurpose.NONE ? null : u;
			}
		}

		@Override
		public String toString() {
			return String.format("Filter(boundaries=%s, css=%s, unit=%s, "
							+ "productionPurpose=%s, usePurpose=%s)",
					boundaries, css, unit, productionPurpose, usePurpose);
		}

	}

	public boolean isMatching(Filter filter) {
		if (filter.boundaries != null && filter.boundaries != boundaries)
			return false;
		if (filter.css != null && filter.css != css)
			return false;
		if (filter.unit != null && filter.unit != unit)
			return false;
		if (filter.productionPurpose != null && filter.productionPurpose != productionPurpose)
			return false;
		if (filter.usePurpose != null && filter.usePurpose != usePurpose)
			return false;

		return true;
	}

	static class Selection {
		private final ArrayList<Template> templates;

		public Selection(Template[] templates) {
			this.templates = new ArrayList<>(List.of(templates));
		}

		void filter(Filter filter) {
			templates.removeIf(template -> !template.isMatching(filter));
		}

		public ArrayList<Template> get() {
			return templates;
		}

	}

}
