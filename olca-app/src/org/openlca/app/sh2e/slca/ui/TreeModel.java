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
import org.openlca.core.matrix.index.TechFlow;
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
				.map(i -> new IndicatorNode(this, i));
		return Stream.concat(cxs, ixs).toArray();
	}

	@Override
	public Object[] getChildren(Object obj) {
		if (obj instanceof CategoryNode cn)
			return cn.childs().toArray(new Node[0]);
		if (obj instanceof IndicatorNode n)
			return n.childs().toArray(new Node[0]);
		return new Object[0];
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

		return obj instanceof IndicatorNode;
	}

	interface Node {

		String name();

		Image icon();

		default String activityVariable() {
			return null;
		}

		default double activityValue() {
			return 0;
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
				_childs.add(new IndicatorNode(tree, i));
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

	static class IndicatorNode implements Node {

		private final TreeModel tree;
		private final SocialIndicatorDescriptor descriptor;
		private final SocialIndicator indicator;
		private final SocialRiskValue riskValue;

		private List<TechFlowNode> _childs;

		IndicatorNode(TreeModel tree, SocialIndicatorDescriptor d) {
			this.tree = tree;
			this.descriptor = d;
			this.indicator = tree.db.get(SocialIndicator.class, d.id);
			this.riskValue = tree.result.riskValueOf(d);
		}

		@Override
		public String name() {
			return Labels.name(indicator);
		}

		@Override
		public Image icon() {
			return Images.get(indicator);
		}

		List<TechFlowNode> childs() {
			if (_childs == null) {
				_childs = TechFlowNode.allOf(this);
			}
			return _childs;
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

		@Override
		public double activityValue() {
			return tree.result.activityValueOf(descriptor);
		}

		@Override
		public SocialRiskValue riskValue() {
			return riskValue;
		}
	}

	static class TechFlowNode implements Node {

		private final IndicatorNode parent;
		private final TechFlow techFlow;
		private SocialRiskValue _riskValue;

		TechFlowNode(IndicatorNode parent, TechFlow techFlow) {
			this.parent = parent;
			this.techFlow = techFlow;
		}

		static List<TechFlowNode> allOf(IndicatorNode parent) {
			if (parent == null)
				return List.of();
			var av = parent.activityValue();
			if (av == 0)
				return List.of();

			var nodes = new ArrayList<TechFlowNode>();
			for (var techFlow : parent.tree.result.techIndex()) {
				var node = new TechFlowNode(parent, techFlow);
				// TODO: read the min-share from the tree config
				var share = Math.abs(node.activityValue() / av);
				if (share >= 0.0001) {
					nodes.add(node);
				}
			}
			return nodes;
		}

		private SocialResult result() {
			return parent.tree.result;
		}

		@Override
		public String name() {
			return Labels.name(techFlow);
		}

		@Override
		public Image icon() {
			return Images.get(techFlow);
		}

		@Override
		public SocialRiskValue riskValue() {
			if (_riskValue != null)
				return _riskValue;
			_riskValue = new SocialRiskValue();
			var level = result().riskLevelOf(parent.descriptor, techFlow);
			if (level != null) {
				_riskValue.put(level, activityValue());
			}
			return _riskValue;
		}

		@Override
		public double activityValue() {
			return result().activityValueOf(parent.descriptor, techFlow);
		}
	}
}
