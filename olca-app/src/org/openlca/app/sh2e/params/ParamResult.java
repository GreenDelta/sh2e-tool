package org.openlca.app.sh2e.params;

import org.openlca.core.model.AllocationMethod;
import org.openlca.core.model.ImpactMethod;
import org.openlca.core.model.ProductSystem;
import org.openlca.core.model.descriptors.ImpactDescriptor;
import org.openlca.core.results.LcaResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

record ParamResult(
		ProductSystem system,
		ImpactMethod method,
		AllocationMethod allocation,
		ParamSeq seq,
		Map<ImpactDescriptor, List<Double>> results
) {

	void append(LcaResult r) {
		var impacts = r.impactIndex();
		if (impacts == null || impacts.isEmpty())
			return;
		for (var impact : impacts) {
			double v = r.getTotalImpactValueOf(impact);
			results.computeIfAbsent(impact, i -> new ArrayList<>()).add(v);
		}
	}

	int count() {
		return seq().count();
	}

	List<ImpactDescriptor> impacts() {
		return new ArrayList<>(results.keySet());
	}
}
