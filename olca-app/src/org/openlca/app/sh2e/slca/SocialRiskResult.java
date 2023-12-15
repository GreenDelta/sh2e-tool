package org.openlca.app.sh2e.slca;

import org.openlca.core.database.IDatabase;
import org.openlca.core.database.SocialIndicatorDao;
import org.openlca.core.matrix.format.MatrixReader;
import org.openlca.core.matrix.solvers.MatrixSolver;
import org.openlca.core.model.descriptors.SocialIndicatorDescriptor;
import org.openlca.core.results.providers.ResultProvider;

import java.util.Optional;
import java.util.Set;

public class SocialRiskResult {

	private final SocialRiskIndex index;
	private final double[] total;
	private final MatrixReader direct;

	private SocialRiskResult(
			SocialRiskIndex index, double[] total, MatrixReader direct) {
		this.index = index;
		this.total = total;
		this.direct = direct;
	}

	public static Optional<SocialRiskResult> calculate(
			IDatabase db, ResultProvider p
	) {
		if (db == null || p == null || p.techIndex() == null)
			return Optional.empty();
		var indicators = new SocialIndicatorDao(db).getDescriptors();
		var index = SocialRiskIndex.of(indicators);
		if (index.isEmpty())
			return Optional.empty();
		var matrix = SocialRiskMatrix.build(db, p.techIndex(), index).orElse(null);
		if (matrix == null)
			return Optional.empty();
		var s = p.scalingVector();
		var solver = MatrixSolver.get();
		var total = solver.multiply(matrix, s);
		matrix.scaleColumns(s);
		return Optional.of(new SocialRiskResult(index, total, matrix));
	}

	public Set<SocialIndicatorDescriptor> indicators() {
		return index.indicators();
	}

	public SocialRiskValue totalResultsOf(SocialIndicatorDescriptor d) {
		var v = new SocialRiskValue();
		index.eachOf(d, (i, entry) -> {
			double num = total[i];
			v.put(entry.level(), num);
		});
		return v;
	}
}
