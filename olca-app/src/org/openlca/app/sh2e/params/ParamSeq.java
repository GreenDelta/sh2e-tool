package org.openlca.app.sh2e.params;

import org.openlca.core.model.ParameterRedef;

import java.util.ArrayList;
import java.util.List;

class ParamSeq {

	private List<List<ParameterRedef>> seqs;

	private ParamSeq(List<List<ParameterRedef>> seqs) {
		this.seqs = seqs;
	}

	static ParamSeq of(List<Param> params, int count) {
		var seqs = new ArrayList<List<ParameterRedef>>(count);
		for (int i = 0; i < count; i++) {
			seqs.add(new ArrayList<>());
		}
		for (var param : params) {
			double incx = (param.end - param.start) / (count - 1);
			for (int i = 0; i < count; i++) {
				var list = seqs.get(i);
				var redef = param.redef.copy();
				redef.value = param.start + i*incx;
				list.add(redef);
			}
		}
		return new ParamSeq(seqs);
	}

	List<ParameterRedef> get(int i) {
		return i >= 0 && i < seqs.size()
				? seqs.get(i)
				: List.of();
	}

	int count() {
		return seqs.size();
	}

}
