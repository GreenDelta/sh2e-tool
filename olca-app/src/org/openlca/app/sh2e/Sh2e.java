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
		record SimpleOption(String label) implements Option {};
	}

	public enum Scope {

		APPLICATION("Intended application", Application.values()),
		MODELLING("Modelling principles", Modelling.values()),
		PROSPECTIVITY("Prospectivity", Prospectivity.values()),
		END_OF_LIFE("End-of-life", EndOfLife.values()),
		CAPITAL_GOODS("Capital goods", CapitalGoods.values()),
		RISK_ASSESSMENT("Risk assessment", RiskAssessment.values()),
		BOUNDARIES("System boundaries", Boundaries.values());

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
}