package org.openlca.app.sh2e.slca;

import org.openlca.core.database.IDatabase;
import org.openlca.core.database.SocialIndicatorDao;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.index.TechFlow;
import org.openlca.core.matrix.index.TechIndex;
import org.openlca.core.matrix.solvers.MatrixSolver;
import org.openlca.core.model.RiskLevel;
import org.openlca.core.model.descriptors.SocialIndicatorDescriptor;
import org.openlca.core.results.providers.ResultProvider;

import java.util.Optional;
import java.util.Set;

public class SocialResult {

	private final SocialMatrixData data;
	private final SocialRiskIndex riskIndex;
	private final double[] totalActivityValues;
	private final double[] totalRiskActivityValues;
	private final MatrixReader directActivityValues;

	private SocialResult(
			SocialMatrixData data,
			SocialRiskIndex riskIndex,
			double[] totalActivityValues,
			double[] totalRiskActivityValues,
			MatrixReader directActivityValues) {
		this.data = data;
		this.riskIndex = riskIndex;
		this.totalActivityValues = totalActivityValues;
		this.totalRiskActivityValues = totalRiskActivityValues;
		this.directActivityValues = directActivityValues;

	}

	public static Optional<SocialResult> calculate(
			IDatabase db, ResultProvider p
	) {
		if (db == null || p == null || p.techIndex() == null)
			return Optional.empty();

		// create the index with social indicators
		var indicators = new SocialIndicatorDao(db).getDescriptors();
		if (indicators.isEmpty())
			return Optional.empty();
		var socialIndex = SocialIndex.of(indicators);

		// fetch the matrix data
		var data = SocialMatrixData.fetch(
				db, p.techIndex(), socialIndex).orElse(null);
		if (data == null)
			return Optional.empty();

		// scale the activity values
		var solver = MatrixSolver.get();
		var s = p.scalingVector();
		var totalActivityValues = solver.multiply(data.activityData(), s);
		var directActivityValues = data.activityData().copy();
		directActivityValues.scaleColumns(s);

		// aggregate the activity values by risk-level
		var riskIndex = SocialRiskIndex.of(indicators);
		var totalRiskActivityValues = new double[riskIndex.size()];
		directActivityValues.iterate((i, j, v) -> {
			var indicator = socialIndex.at(i);
			var techFlow = data.techIndex().at(j);
			var level = data.levelData().get(indicator, techFlow);
			if (level == null)
				return;
			int k = riskIndex.of(indicator, level);
			if (k < 0)
				return;
			totalRiskActivityValues[k] += v;
		});

		var r = new SocialResult(
				data,
				riskIndex,
				totalActivityValues,
				totalRiskActivityValues,
				directActivityValues
		);
		return Optional.of(r);
	}

	public TechIndex techIndex() {
		return data.techIndex();
	}

	public Set<SocialIndicatorDescriptor> indicators() {
		return data.socialIndex().content();
	}

	public SocialRiskValue riskValueOf(SocialIndicatorDescriptor d) {
		var v = new SocialRiskValue();
		riskIndex.eachOf(d, (i, entry) -> {
			double num = totalRiskActivityValues[i];
			v.put(entry.level(), num);
		});
		return v;
	}

	public double activityValueOf(SocialIndicatorDescriptor d) {
		if (d == null)
			return 0;
		int i = data.socialIndex().of(d);
		return i >= 0
				? totalActivityValues[i]
				: 0;
	}

	public double activityValueOf(SocialIndicatorDescriptor d, TechFlow techFlow) {
		if (d == null || techFlow == null)
			return 0;
		int i = data.socialIndex().of(d);
		int j = data.techIndex().of(techFlow);
		return i >= 0 && j >= 0
				? directActivityValues.get(i, j)
				: 0;
	}

	public RiskLevel riskLevelOf(SocialIndicatorDescriptor d, TechFlow techFlow) {
		return d != null || techFlow != null
				? data.levelData().get(d, techFlow)
				: null;
	}
}
