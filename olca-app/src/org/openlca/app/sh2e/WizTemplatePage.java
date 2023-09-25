package org.openlca.app.sh2e;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.Controls;
import org.openlca.app.util.UI;


class WizTemplatePage extends WizardPage {

	private String category;
	private Template selected;

	private ArrayList<Template> templates = new ArrayList<>();
	private final Template.Filter filter = new Template.Filter();
	private List list;
	private Label label;

	WizTemplatePage() {
		super("WizTemplatePage");
		setTitle("Select a template");
		setDescription("Please select a matching template " +
				"and a top-category under which the template should be stored");
		setPageComplete(false);
	}

	void addFilter(Option... options) {
		filter.add(options);
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

		label = UI.label(body, "Select a template:");
		UI.fillHorizontal(label);
		list = new List(body, SWT.BORDER);
		UI.gridData(list, true, true);

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

	protected void fillList() {
		if (list == null)
			return;
		selected = null;
		setPageComplete(false);
		var selection = new Template.Selection(Template.values());
		System.out.println(filter);
		selection.filter(filter);
		templates = selection.get();
		var items = templates.stream().map(Template::label).toArray(String[]::new);
		list.setItems(items);
		if (list.getItemCount() == 0) {
			var output = Template.labelOf(
					filter.boundaries,
					filter.css,
					filter.unit,
					filter.productionPurpose,
					filter.usePurpose);
			label.setText("No template available for this setup: " + output);
		} else {
			label.setText("Select a template:");
		}
	}

}
