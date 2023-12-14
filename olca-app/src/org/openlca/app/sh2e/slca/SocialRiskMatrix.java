package org.openlca.app.sh2e.slca;

import gnu.trove.map.hash.TLongObjectHashMap;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.NativeSql;
import org.openlca.core.matrix.format.Matrix;
import org.openlca.core.matrix.format.MatrixBuilder;
import org.openlca.core.matrix.index.TechIndex;
import org.openlca.core.model.RiskLevel;
import org.openlca.core.model.descriptors.SocialIndicatorDescriptor;

import java.util.Optional;

public class SocialRiskMatrix {

	private final IDatabase db;
	private final TechIndex techIndex;
	private final SocialRiskIndex riskIndex;
	private final TLongObjectHashMap<SocialIndicatorDescriptor> indicators;

	private SocialRiskMatrix(
		IDatabase db, TechIndex techIndex, SocialRiskIndex riskIndex
	) {
		this.db = db;
		this.techIndex = techIndex;
		this.riskIndex = riskIndex;
		this.indicators = new TLongObjectHashMap<>();
		for (var e : riskIndex) {
			var indicator = e.indicator();
			if (!indicators.containsKey(indicator.id)) {
				indicators.put(indicator.id, indicator);
			}
		}
	}

	public static Optional<Matrix> build(
		IDatabase db, TechIndex techIndex, SocialRiskIndex riskIndex
	) {
		if (db == null
			|| techIndex == null
			|| techIndex.isEmpty()
			|| riskIndex == null
			|| riskIndex.isEmpty())
			return Optional.empty();
		var builder = new SocialRiskMatrix(db, techIndex, riskIndex);
		return Optional.of(builder.createMatrix());
	}

	private Matrix createMatrix() {
		var m = new MatrixBuilder();
		m.minSize(riskIndex.size(), techIndex.size());
		var q = "select " +
			"f_process, " +
			"f_indicator, " +
			"risk_level, " +
			"activity_value from tbl_social_aspects";
		NativeSql.on(db).query(q, r -> {
			long processId = r.getLong(1);
			var techFlows = techIndex.getProviders(processId);
			if (techFlows.isEmpty())
				return true;
			var indicator = indicators.get(r.getLong(2));
			var riskLevel = riskLevelOf(r.getString(3));
			if (indicator == null || riskLevel == null)
				return true;
			int i = riskIndex.of(indicator, riskLevel);
			if (i < 0)
				return true;
			double value = r.getDouble(4);
			for (var techFlow : techFlows) {
				int j = techIndex.of(techFlow);
				m.set(i, j, value);
			}
			return true;
		});
		return m.finish();
	}

	private RiskLevel riskLevelOf(String name) {
		try {
			return RiskLevel.valueOf(name);
		} catch (Exception e) {
			return null;
		}
	}
}
