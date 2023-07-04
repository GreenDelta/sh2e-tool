package org.openlca.app.sh2e;

import org.openlca.util.Strings;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

final class Sh2e {

	public interface Option {
		String label();

		Option Yes = new SimpleOption("Yes");
		Option No = new SimpleOption("No");
		Option Empty = new SimpleOption("--");

		static Option from(String label) {
			if (Strings.nullOrEmpty(label))
				return Empty;
			var s = label.strip().toLowerCase(Locale.US);
			return switch (s) {
				case "yes" -> Yes;
				case "no" -> No;
				default -> new SimpleOption(label);
			};
		}

		record SimpleOption(String label) implements Option {
		}
	}

	public enum Scope {

		APPLICATION("Intended application", Application.values()),
		MODELLING("Modelling principles", Modelling.values()),
		PROSPECTIVITY("Prospectivity", Prospectivity.values()),
		END_OF_LIFE("End-of-life", EndOfLife.values()),
		CAPITAL_GOODS("Capital goods", CapitalGoods.values()),
		RISK_ASSESSMENT("Risk assessment", RiskAssessment.values()),
		BOUNDARIES("System boundaries", Boundaries.values()),
		TECHNOLOGY_LEVEL("Technology readiness level", TechnologyLevel.values()),
		CRADLE_TO_GATE("Boundary of hydrogen production", CradleToGate.values());

		private final String label;
		private final Option[] options;

		Scope(String label, Option[] options) {
			this.label = label;
			this.options = options;
		}

		public static Optional<Scope> of(String label) {
			if (label == null)
				return Optional.empty();
			for (var g : values()) {
				if (label.equals(g.label))
					return Optional.of(g);
			}
			return Optional.empty();
		}

		public String label() {
			return label;
		}

		public List<Option> options() {
			return List.of(options);
		}
	}

	static Option optionOf(String label, Option[] options) {
		if (label == null)
			return Option.Empty;
		for (var opt : options) {
			if (label.equals(opt.label()))
				return opt;
		}
		return Option.from(label);
	}

	public enum Application implements Option {

		MICRO("Micro-level decision support"),

		MACRO("Meso/Macro-level decision support"),

		ACCOUNTING("Accounting");

		private final String label;

		Application(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum Modelling implements Option {

		ATTRIBUTIONAL("Attributional"),

		CONSEQUENTIAL("Consequential");

		private final String label;

		Modelling(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum Prospectivity implements Option {

		PROSPECTIVE("Prospective study"),

		RETROSPECTIVE("Retrospective study");

		private final String label;

		Prospectivity(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum EndOfLife implements Option {

		CUTOFF("Cut-off approach"),

		AVOIDED("Recycling approach"),

		CIRCULAR("Circular footprint formula"),

		OTHER("Other approach, please state");

		private final String label;

		EndOfLife(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum CapitalGoods implements Option {

		INCLUDED("Included"),

		EXCLUDED("Excluded");

		private final String label;

		CapitalGoods(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum RiskAssessment implements Option {

		INCLUDED("Risk based processes included"),

		EXCLUDED("Simple LCA, without risk assessment");

		private final String label;

		RiskAssessment(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum Boundaries implements Option {

		PRODUCTION("Hydrogen production"),

		USE("Hydrogen use"),

		NONE("Hydrogen production and use");

		private final String label;

		Boundaries(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum TechnologyLevel implements Option {

		TRL_1("TRL 1 - basic principles observed"),
		TRL_2("TRL 2 - technology concept formulated"),
		TRL_3("TRL 3 - experimental proof of concept"),
		TRL_4("TRL 4 - technology validated in lab"),
		TRL_5("TRL 5 - technology validated in relevant environment"),
		TRL_6("TRL 6 - technology demonstrated in relevant environment"),
		TRL_7("TRL 7 - system prototype demonstration in operational environment"),
		TRL_8("TRL 8 - system complete and qualified"),
		TRL_9("TRL 9 - actual system proven in operational environment");

		private final String label;

		TechnologyLevel(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum CradleToGate implements Option {

		C2G_1("Cradle-to-gate 1"),
		C2G_2("Cradle-to-gate 2"),
		C2G_3("Cradle-to-gate 3"),
		C2G_4("Cradle-to-gate 4"),
		C2G_5("Cradle-to-gate 5"),
		C2G_6("Cradle-to-gate 6");

		private final String label;

		CradleToGate(String label) {
			this.label = label;
		}

		public static Option of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}
}
