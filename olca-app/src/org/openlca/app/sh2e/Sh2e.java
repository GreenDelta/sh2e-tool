package org.openlca.app.sh2e;

import java.util.List;
import java.util.Optional;

public final class Sh2e {

	public interface Option {
		String label();
	}

	public enum Group {
		APPLICATION("Intended application", Application.values()),
		MODELLING("Modelling principles", Modelling.values()),
		PROSPECTIVITY("Prospectivity", Prospectivity.values()),
		SCALING("Scaling and learning curves", Scaling.values()),
		END_OF_LIFE("End of life", EndOfLife.values()),
		CAPITAL_GOODS("Capital goods", CapitalGoods.values()),
		RISK_ASSESSMENT("Risk assessment", RiskAssessment.values()),
		THRESHOLD("Threshold and loops", Threshold.values());

		private final String label;
		private final Option[] options;

		Group(String label, Option[] options) {
			this.label = label;
			this.options = options;
		}

		public static Optional<Group> of(String label) {
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

	private static <E extends Option> Optional<E> optionOf(
			String label, E[] options
	) {
		if (label == null)
			return Optional.empty();
		for (var opt : options) {
			if (label.equals(opt.label()))
				return Optional.of(opt);
		}
		return Optional.empty();
	}

	public enum Application implements Option {

		MICRO("Micro-level decision support"),

		MACRO("Meso/Macro-level decision support"),

		ACCOUNTING("Accounting");

		private final String label;

		Application(String label) {
			this.label = label;
		}

		public static Optional<Application> of(String label) {
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

		public static Optional<Modelling> of(String label) {
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

		public static Optional<Prospectivity> of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum Scaling implements Option {

		INCLUDED("Included"),

		EXCLUDED("Excluded");

		private final String label;

		Scaling(String label) {
			this.label = label;
		}

		public static Optional<Scaling> of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum EndOfLife implements Option {

		CUTOFF("Cut-off"),

		AVOIDED("Avoided burden approach"),

		CIRCULAR("Circular footprint formula");

		private final String label;

		EndOfLife(String label) {
			this.label = label;
		}

		public static Optional<EndOfLife> of(String label) {
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

		public static Optional<CapitalGoods> of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum RiskAssessment implements Option {

		INCLUDED("Included"),

		EXCLUDED("Excluded");

		private final String label;

		RiskAssessment(String label) {
			this.label = label;
		}

		public static Optional<RiskAssessment> of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}

	public enum Threshold implements Option {

		INCLUDED("Included"),

		EXCLUDED("Excluded");

		private final String label;

		Threshold(String label) {
			this.label = label;
		}

		public static Optional<Threshold> of(String label) {
			return Sh2e.optionOf(label, values());
		}

		public String label() {
			return label;
		}
	}
}
