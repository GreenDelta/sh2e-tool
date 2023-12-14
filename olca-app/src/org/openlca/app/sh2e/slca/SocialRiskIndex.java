package org.openlca.app.sh2e.slca;

import org.openlca.core.matrix.index.IndexConsumer;
import org.openlca.core.matrix.index.MatrixIndex;
import org.openlca.core.model.RiskLevel;
import org.openlca.core.model.descriptors.SocialIndicatorDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialRiskIndex implements MatrixIndex<SocialRiskEntry> {

	private final ArrayList<SocialRiskEntry> content = new ArrayList<>();
	private final Map<SocialRiskEntry, Integer> index = new HashMap<>();

	private SocialRiskIndex() {
	}

	public static SocialRiskIndex of(List<SocialIndicatorDescriptor> ds) {
		if (ds == null || ds.isEmpty())
			return empty();
		var idx = new SocialRiskIndex();
		for (var indicator : ds) {
			for (var level : RiskLevel.values()) {
				idx.add(SocialRiskEntry.of(indicator, level));
			}
		}
		return idx;
	}

	public static SocialRiskIndex empty() {
		return new SocialRiskIndex();
	}

	@Override
	public int size() {
		return content.size();
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public SocialRiskEntry at(int i) {
		return i >= 0 && i < content.size()
			? content.get(i)
			: null;
	}

	@Override
	public int of(SocialRiskEntry e) {
		if (e == null)
			return -1;
		var i = index.get(e);
		return i != null ? i : -1;
	}

	public int of(SocialIndicatorDescriptor d, RiskLevel r) {
		if (d == null || r == null)
			return -1;
		// TODO: avoid object allocations with a smarter index structure
		return of(SocialRiskEntry.of(d, r));
	}

	@Override
	public boolean contains(SocialRiskEntry e) {
		return of(e) >= 0;
	}

	// TODO public void contains(SocialIndicatorDescriptor d)

	@Override
	public int add(SocialRiskEntry e) {
		if (e == null)
			return -1;
		int idx = of(e);
		if (idx >= 0)
			return idx;
		idx = content.size();
		content.add(e);
		index.put(e, idx);
		return idx;
	}

	@Override
	public void each(IndexConsumer<SocialRiskEntry> fn) {
		for (int i = 0; i < content.size(); i++) {
			fn.accept(i, content.get(i));
		}
	}

	@Override
	public Set<SocialRiskEntry> content() {
		return new HashSet<>(content);
	}

	@Override
	public MatrixIndex<SocialRiskEntry> copy() {
		var copy = new SocialRiskIndex();
		for (var e : content) {
			copy.add(e);
		}
		return copy;
	}

	@Override
	public Iterator<SocialRiskEntry> iterator() {
		return Collections.unmodifiableList(content).iterator();
	}
}
