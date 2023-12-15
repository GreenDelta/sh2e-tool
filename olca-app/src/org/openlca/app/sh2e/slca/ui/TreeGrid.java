package org.openlca.app.sh2e.slca.ui;

import org.openlca.core.model.RiskLevel;

class TreeGrid {

	static String headerOf(RiskLevel level) {
		if (level == null)
			return "?";
		return switch (level) {
			case HIGH_OPPORTUNITY -> "HO";
			case MEDIUM_OPPORTUNITY -> "MO";
			case LOW_OPPORTUNITY -> "LO";
			case NO_OPPORTUNITY -> "NOP";
			case NO_RISK -> "NOR";
			case VERY_LOW_RISK -> "VLR";
			case LOW_RISK -> "LR";
			case MEDIUM_RISK -> "MR";
			case HIGH_RISK -> "HR";
			case VERY_HIGH_RISK -> "VHR";
			case NO_DATA -> "ND";
			case NOT_APPLICABLE -> "NA";
		};
	}

	static int columnOf(RiskLevel level) {
		if (level == null)
			return -1;
		return 2 + switch (level) {
			case HIGH_OPPORTUNITY -> 0;
			case MEDIUM_OPPORTUNITY -> 1;
			case LOW_OPPORTUNITY -> 2;
			case NO_OPPORTUNITY -> 3;
			case NO_RISK -> 4;
			case VERY_LOW_RISK -> 5;
			case LOW_RISK -> 6;
			case MEDIUM_RISK -> 7;
			case HIGH_RISK -> 8;
			case VERY_HIGH_RISK -> 9;
			case NO_DATA -> 10;
			case NOT_APPLICABLE -> 11;
		};
	}
}
