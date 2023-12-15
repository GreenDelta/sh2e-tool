package org.openlca.app.sh2e.slca.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.openlca.app.db.Database;
import org.openlca.app.rcp.images.Images;
import org.openlca.app.sh2e.slca.SocialRiskResult;
import org.openlca.app.sh2e.slca.SocialRiskValue;
import org.openlca.app.util.Labels;
import org.openlca.core.database.CategoryDao;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.Category;
import org.openlca.core.model.ModelType;
import org.openlca.core.model.SocialIndicator;
import org.openlca.core.model.descriptors.SocialIndicatorDescriptor;
import org.openlca.util.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class TreeModel implements ITreeContentProvider {

	private final SocialRiskResult result;
	private final IDatabase db;
	private final Map<Long, List<SocialIndicatorDescriptor>> categoryIndex;

	TreeModel(SocialRiskResult result) {
		this.result = result;
		this.db = Database.get();
		categoryIndex = new HashMap<>();
		for (var i : result.indicators()) {
			categoryIndex.computeIfAbsent(
					i.category, $ -> new ArrayList<>()).add(i);
		}
	}

	@Override
	public Object[] getElements(Object obj) {
		if (!(obj instanceof TreeModel))
			return new Object[0];
		var roots = new CategoryDao(db)
				.getRootCategories(ModelType.SOCIAL_INDICATOR);
		return childsOf(null, roots);
	}

	@Override
	public Object[] getChildren(Object obj) {
		return obj instanceof CategoryNode cn
				? childsOf(cn.category.id, cn.category.childCategories)
				: new Object[0];
	}

	private Object[] childsOf(Long root, List<Category> categories) {
		var cxs = categories.stream()
				.filter(this::hasChildren)
				.map(CategoryNode::new)
				.sorted();
		var indicators = categoryIndex.get(root);
		if (indicators == null || indicators.isEmpty())
			return cxs.toArray();
		var ixs = indicators.stream()
				.map(i -> IndicatorNode.of(db, result, i))
				.sorted();
		return Stream.concat(cxs, ixs).toArray();
	}

	@Override
	public Object getParent(Object obj) {
		return null;
	}

	@Override
	public boolean hasChildren(Object obj) {
		if (obj instanceof CategoryNode cn)
			return hasChildren(cn.category);

		if (obj instanceof Category c) {
			var ixs = categoryIndex.get(c.id);
			if (ixs != null && !ixs.isEmpty())
				return true;
			for (var child : c.childCategories) {
				if (hasChildren(child))
					return true;
			}
			return false;
		}

		return false;
	}

	interface Node extends Comparable<Node> {

		String name();

		Image icon();

		default String variable() {
			return null;
		}

		default SocialRiskValue value() {
			// TODO: new default!
			return new SocialRiskValue();
		}

		@Override
		default int compareTo(Node other) {
			return other != null
					? Strings.compare(this.name(), other.name())
					: 1;
		}
	}

	private record CategoryNode(Category category) implements Node {

		@Override
		public String name() {
			return Labels.name(category);
		}

		@Override
		public Image icon() {
			return Images.get(category);
		}
	}

	private record IndicatorNode(
			SocialIndicatorDescriptor descriptor,
			SocialIndicator indicator,
			SocialRiskValue value
	) implements Node {

		static IndicatorNode of(
				IDatabase db, SocialRiskResult r, SocialIndicatorDescriptor d
		) {
			var value = r.totalResultsOf(d);
			var indicator = db.get(SocialIndicator.class, d.id);
			return new IndicatorNode(d, indicator, value);
		}

		@Override
		public String name() {
			return Labels.name(indicator);
		}

		@Override
		public Image icon() {
			return Images.get(indicator);
		}

		@Override
		public String variable() {
			if (indicator == null)
				return null;
			var u = indicator.activityUnit != null
					? indicator.activityUnit.name
					: null;
			var v = indicator.activityVariable;
			if (Strings.nullOrEmpty(u))
				return v;
			if (Strings.nullOrEmpty(v))
				return u;
			return v + " [" + u + "]";
		}
	}
}
