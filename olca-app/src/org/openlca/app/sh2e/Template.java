package org.openlca.app.sh2e;

import org.openlca.app.sh2e.Sh2e.Boundaries;
import org.openlca.app.sh2e.Sh2e.CSS;
import org.openlca.app.sh2e.Sh2e.FunctionalUnit;
import org.openlca.app.sh2e.Sh2e.ProductionPurpose;
import org.openlca.app.sh2e.Sh2e.UsePurpose;

import java.util.ArrayList;
import java.util.List;

enum Template {

	P_KG("Case1-Cradle-to-Gate1-kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	P_KG_CSS("Case1-Cradle-to-Gate1-kg-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	P_MJ("Case1-Cradle-to-Gate1-MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	P_MJ_CSS("Case1-Cradle-to-Gate1-MJ-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.NONE),
	PU_KG("Case1-Cradle-to-Gate2-kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	PU_KG_CSS("Case1-Cradle-to-Gate2-kg-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	PU_MJ("Case1-Cradle-to-Gate2-MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	PU_MJ_CSS("Case1-Cradle-to-Gate2-MJ-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.PURIFICATION,
			UsePurpose.NONE),
	C_KG("Case1-Cradle-to-Gate3-kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	C_KG_CSS("Case1-Cradle-to-Gate3-kg-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	C_MJ("Case1-Cradle-to-Gate3-MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	C_MJ_CSS("Case1-Cradle-to-Gate3-MJ-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.COMPRESSION,
			UsePurpose.NONE),
	P_T_KG("Case1-Cradle-to-Gate4-kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	P_T_KG_CSS("Case1-Cradle-to-Gate4-kg-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	P_T_MJ("Case1-Cradle-to-Gate4-MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	P_T_MJ_CSS("Case1-Cradle-to-Gate4-MJ-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.TRANSPORTATION,
			UsePurpose.NONE),
	S_KG("Case1-Cradle-to-Gate5-kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	S_KG_CSS("Case1-Cradle-to-Gate5-kg-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	S_MJ("Case1-Cradle-to-Gate5-MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	S_MJ_CSS("Case1-Cradle-to-Gate5-MJ-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.STORAGE,
			UsePurpose.NONE),
	D_KG("Case1-Cradle-to-Gate6-kg.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),
	D_KG_CSS("Case1-Cradle-to-Gate6-kg-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.KG_H2,
			CSS.WITH_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),
	D_MJ("Case1-Cradle-to-Gate6-MJ.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITHOUT_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),
	D_MJ_CSS("Case1-Cradle-to-Gate6-MJ-withCCS.zip",
			Boundaries.PRODUCTION,
			FunctionalUnit.MJ_H2,
			CSS.WITH_CSS,
			ProductionPurpose.DISTRIBUTION,
			UsePurpose.NONE),

	CO("Case2-HydrogenUse-Cogeneration-MJ.zip",
			Boundaries.USE,
			FunctionalUnit.MJ_COGENERATION,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.HEAT),
	E("Case2-HydrogenUse-ElectricityGeneration-MJ.zip",
			Boundaries.USE,
			FunctionalUnit.MJ_ELEC,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.ELECTRICITY),
	CH("Case2-HydrogenUse-FuelorChemicalProduction-kg.zip",
			Boundaries.USE,
			FunctionalUnit.KG_FUEL_CHEMICAL,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.CHEMICALS),
	F("Case2-HydrogenUse-FuelProduction-MJ.zip",
			Boundaries.USE,
			FunctionalUnit.MJ_FUEL,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.FUELS),
	U_T_KM("Case2-HydrogenUse-Transportation-km.zip",
			Boundaries.USE,
			FunctionalUnit.KM,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.TRANSPORTATION),
	U_T_PKM("Case2-HydrogenUse-Transportation-p.km.zip",
			Boundaries.USE,
			FunctionalUnit.PASSENGER_LOAD,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.TRANSPORTATION),
	U_T_TKM("Case2-HydrogenUse-Transportation-t.km.zip",
			Boundaries.USE,
			FunctionalUnit.FREIGHT_LOAD,
			CSS.WITHOUT_CSS,
			ProductionPurpose.NONE,
			UsePurpose.TRANSPORTATION),

	N_C_MJ("Case3-HydrogenProduction&Use-Cogeneration-MJ.zip",
			Boundaries.NONE,
			FunctionalUnit.MJ_COGENERATION,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.HEAT),
	N_C_MJ_CSS("Case3-HydrogenProduction&Use-Cogeneration-MJ-withCCS.zip",
			Boundaries.NONE,
			FunctionalUnit.MJ_COGENERATION,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.HEAT),
	N_E_MJ("Case3-HydrogenProduction&Use-ElectricityGeneration-MJ.zip",
			Boundaries.NONE,
			FunctionalUnit.MJ_ELEC,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.ELECTRICITY),
	N_E_MJ_CSS("Case3-HydrogenProduction&Use-ElectricityGeneration-MJ-withCCS.zip",
			Boundaries.NONE,
			FunctionalUnit.MJ_ELEC,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.ELECTRICITY),
	N_CH_KG("Case3-HydrogenProduction&Use-Fuel&ChemicalProduction-kg.zip",
			Boundaries.NONE,
			FunctionalUnit.KG_FUEL_CHEMICAL,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.CHEMICALS),
	N_CH_KG_CSS("Case3-HydrogenProduction&Use-Fuel&ChemicalProduction-kg-withCCS.zip",
			Boundaries.NONE,
			FunctionalUnit.KG_FUEL_CHEMICAL,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.CHEMICALS),
	N_F_KG("Case3-HydrogenProduction&Use-FuelProduction-MJ.zip",
			Boundaries.NONE,
			FunctionalUnit.MJ_FUEL,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.FUELS),
	N_F_KG_CSS("Case3-HydrogenProduction&Use-FuelProduction-MJ-withCCS.zip",
			Boundaries.NONE,
			FunctionalUnit.MJ_FUEL,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.FUELS),
	N_T_KM("Case3-HydrogenProduction&Use-Transportation-km.zip",
			Boundaries.NONE,
			FunctionalUnit.KM,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION),
	N_T_KM_CSS("Case3-HydrogenProduction&Use-Transportation-km-withCCS.zip",
			Boundaries.NONE,
			FunctionalUnit.KM,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION),
	N_T_PKM("Case3-HydrogenProduction&Use-Transportation-p.km.zip",
			Boundaries.NONE,
			FunctionalUnit.PASSENGER_LOAD,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION),
	N_T_PKM_CSS("Case3-HydrogenProduction&Use-Transportation-p.km-withCCS.zip",
			Boundaries.NONE,
			FunctionalUnit.PASSENGER_LOAD,
			CSS.WITH_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION),
	N_T_TKM("Case3-HydrogenProduction&Use-Transportation-t.km.zip",
			Boundaries.NONE,
			FunctionalUnit.FREIGHT_LOAD,
			CSS.WITHOUT_CSS,
			ProductionPurpose.PRODUCTION,
			UsePurpose.TRANSPORTATION),
	N_T_TKM_CSS("Case3-HydrogenProduction&Use-Transportation-t.km-withCCS.zip",
			Boundaries.NONE,
			FunctionalUnit.FREIGHT_LOAD,
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
