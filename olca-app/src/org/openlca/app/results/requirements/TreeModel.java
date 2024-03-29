package org.openlca.app.results.requirements;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.openlca.core.results.LcaResult;

class TreeModel implements ITreeContentProvider {

	List<ProviderItem> providers;
	List<CategoryItem> categories;

	private final LcaResult result;
	private final Costs costs;
	private final Object[] empty = new Object[0];

	TreeModel(LcaResult result, Costs costs) {
		this.result = result;
		this.costs = costs;
	}

	@Override
	public Object[] getElements(Object input) {
		if (!(input instanceof LcaResult))
			return empty;
		providers = ProviderItem.allOf(result, costs);
		if (providers.size() < 20) {
			return providers.toArray();
		}
		categories = CategoryItem.allOf(providers);
		var uncat = providers.stream().filter(
			p -> p.categoryID() == null);
		return Stream.concat(categories.stream(), uncat).toArray();
	}

	@Override
	public Object[] getChildren(Object elem) {
		if (!(elem instanceof Item item))
			return empty;
		if (item.isProvider())
			return ChildItem
				.allOf(item.asProvider(), result)
				.toArray();
		if (!item.isCategory() || providers == null)
			return empty;
		var catItem = item.asCategory();
		var providers = this.providers.stream()
			.filter(p -> p.categoryID() != null
				&& p.categoryID() == catItem.category.id);
		return Stream.concat(catItem.childs.stream(), providers).toArray();
	}

	@Override
	public boolean hasChildren(Object elem) {
		if (!(elem instanceof Item item))
			return false;
		return item.isProvider() || item.isCategory();
	}

	@Override
	public Object getParent(Object elem) {
		if (!(elem instanceof ChildItem child))
			return null;
		return child.parent;
	}
}
