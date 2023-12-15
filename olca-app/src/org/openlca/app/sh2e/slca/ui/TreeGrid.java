package org.openlca.app.sh2e.slca.ui;

import org.openlca.app.util.Labels;
import org.openlca.core.model.RiskLevel;

class TreeGrid {

	static String headerOf(RiskLevel level) {
		if (level == null)
			return "?";
		return switch (level) {
			case HIGH_OPPORTUNITY -> "HO";
			case MEDIUM_OPPORTUNITY -> "MO";
			case LOW_OPPORTUNITY -> "LO";
			case NO_OPPORTUNITY, NO_RISK, NOT_APPLICABLE -> "NO";
			case VERY_LOW_RISK -> "VLR";
			case LOW_RISK -> "LR";
			case MEDIUM_RISK -> "MR";
			case HIGH_RISK -> "HR";
			case VERY_HIGH_RISK -> "VHR";
			case NO_DATA -> "ND";
		};
	}

	static String tooltipOf(RiskLevel level) {
		if (level == null)
			return "";
		return switch (level) {
			case NO_OPPORTUNITY, NO_RISK, NOT_APPLICABLE ->
					"No risk, no opportunity, or not applicable";
			default -> Labels.of(level);
		};
	}

	static int columnOf(RiskLevel level) {
		if (level == null)
			return -1;
		return 2 + switch (level) {
			case HIGH_OPPORTUNITY -> 0;
			case MEDIUM_OPPORTUNITY -> 1;
			case LOW_OPPORTUNITY -> 2;
			case NO_OPPORTUNITY, NO_RISK, NOT_APPLICABLE -> 3;
			case VERY_LOW_RISK -> 4;
			case LOW_RISK -> 5;
			case MEDIUM_RISK -> 6;
			case HIGH_RISK -> 7;
			case VERY_HIGH_RISK -> 8;
			case NO_DATA -> 9;
		};
	}


}
