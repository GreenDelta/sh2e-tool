package org.openlca.app.sh2e.slca.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.openlca.app.db.Database;
import org.openlca.app.rcp.images.Images;
import org.openlca.app.sh2e.slca.SocialResult;
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

	private final SocialResult result;
	private final IDatabase db;
	private final Map<Long, List<SocialIndicatorDescriptor>> categoryIndex;

	TreeModel(SocialResult result) {
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
		var cxs = new CategoryDao(db)
				.getRootCategories(ModelType.SOCIAL_INDICATOR)
				.stream()
				.filter(this::hasChildren)
				.map(cx -> new CategoryNode(this, cx));
		var indicators = categoryIndex.get(null);
		if (indicators == null || indicators.isEmpty())
			return cxs.toArray();
		var ixs = indicators.stream()
				.map(i -> IndicatorNode.of(this, i));
		return Stream.concat(cxs, ixs).toArray();
	}

	@Override
	public Object[] getChildren(Object obj) {
		return obj instanceof CategoryNode cn
				? cn.childs().toArray(new Node[0])
				: new Object[0];
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

	interface Node {

		String name();

		Image icon();

		default String activityVariable() {
			return null;
		}

		default Double activityValue() {
			return null;
		}

		SocialRiskValue riskValue();
	}

	static class CategoryNode implements Node {

		private final TreeModel tree;
		private final Category category;
		private List<Node> _childs;
		private SocialRiskValue _value;

		CategoryNode(TreeModel tree, Category category) {
			this.tree = tree;
			this.category = category;
		}

		@Override
		public String name() {
			return Labels.name(category);
		}

		@Override
		public Image icon() {
			return Images.get(category);
		}

		List<Node> childs() {
			if (_childs != null)
				return _childs;
			_childs = new ArrayList<>();
			for (var c : category.childCategories) {
				if (!tree.hasChildren(c))
					continue;
				var n = new CategoryNode(tree, c);
				_childs.add(n);
			}
			var indicators = tree.categoryIndex.get(category.id);
			if (indicators == null)
				return _childs;
			for (var i : indicators) {
				_childs.add(IndicatorNode.of(tree, i));
			}
			return _childs;
		}

		@Override
		public SocialRiskValue riskValue() {
			if (_value != null)
				return _value;
			_value = new SocialRiskValue();
			for (var c : childs()) {
				var cv = c.riskValue();
				for (int i = 0; i < cv.size(); i++) {
					_value.add(i, cv.getShare(i));
				}
			}
			return _value;
		}
	}

	record IndicatorNode(
			SocialIndicatorDescriptor descriptor,
			SocialIndicator indicator,
			SocialRiskValue riskValue,
			Double activityValue
	) implements Node {

		static IndicatorNode of(
				TreeModel tree, SocialIndicatorDescriptor d
		) {
			var riskValue = tree.result.riskValueOf(d);
			var indicator = tree.db.get(SocialIndicator.class, d.id);
			var activityValue = tree.result.activityValueOf(d);
			return new IndicatorNode(d, indicator, riskValue, activityValue);
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
		public String activityVariable() {
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
