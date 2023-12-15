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


}
