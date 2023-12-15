package org.openlca.app.sh2e.slca;

import org.openlca.core.model.RiskLevel;

public class SocialRiskValue {

	// values() allocates an array each time it is called
	// but there is no easy other way to get the number
	// of constants of an enum type, thus we cache it
	private static final int N = RiskLevel.values().length;

	private final double[] values = new double[N];

	void put(RiskLevel level, double value) {
		if (level == null)
			return;
		values[level.ordinal()] = value;
	}

	public double get(RiskLevel level) {
		return level != null
				? values[level.ordinal()]
				: 0;
	}

	public double getShare(RiskLevel level) {
		if (level == null)
			return 0;
		double max = max();
		return max != 0
				? Math.abs(get(level)) / max
				: 0;
	}

	private double max() {
		double m = Math.abs(values[0]);
		for (int i = 1; i < values.length; i++) {
			m = Math.max(m, Math.abs(values[i]));
		}
		return m;
	}
}
