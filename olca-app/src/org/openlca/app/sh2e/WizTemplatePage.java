package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.openlca.app.sh2e.Sh2e.Boundaries;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.Controls;
import org.openlca.app.util.UI;

import java.util.ArrayList;

class WizTemplatePage extends WizardPage {

	private String category;
	private Template selected;

	private final ArrayList<Template> templates = new ArrayList<>();
	private Boundaries filter;
	private List list;

	WizTemplatePage() {
		super("WizTemplatePage");
		setTitle("Select a template");
		setDescription("Please select a matching template " +
				"and a top-category under which the template should be stored");
		setPageComplete(false);
	}

	void setFilter(Option option) {
		Boundaries filter = null;
		for (var b : Boundaries.values()) {
			if (b == option) {
				filter = b;
				break;
			}
		}
		this.filter = filter;
		fillList();
	}

	public String category() {
		return category;
	}

	public Template template() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var top = UI.composite(body);
		UI.fillHorizontal(top);
		UI.gridLayout(top, 2, 10, 0);
		var text = UI.labeledText(top, "Category");
		text.addModifyListener(
				e -> category = text.getText().strip());

		UI.label(body, "Select a template:");
		list = new List(body, SWT.BORDER);
		UI.gridData(list, true, true);
		fillList();

		Controls.onSelect(list, $ -> {
			int idx = list.getSelectionIndex();
			if (idx < 0 || idx >= templates.size()) {
				selected = null;
				setPageComplete(false);
			} else {
				selected = templates.get(idx);
				setPageComplete(true);
			}
		});
	}

	private void fillList() {
		if (list == null)
			return;
		templates.clear();
		selected = null;
		setPageComplete(false);
		var items = new ArrayList<String>();
		for (var t : Template.values()) {
			if (matchesFilter(t)) {
				templates.add(t);
						items.add(t.label());
			}
		}
		list.setItems(items.toArray(new String[0]));
	}

	private boolean matchesFilter(Template template) {
		return filter == null
				|| filter == Boundaries.NONE
				|| template.boundaries() == filter;
	}
}
