package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.openlca.app.util.Controls;
import org.openlca.app.util.UI;

class WizTemplatePage extends WizardPage {

	private String category;
	private Template template;

	WizTemplatePage() {
		super("WizTemplatePage");
		setTitle("Select a template");
		setDescription("Please select a matching template " +
				"and a top-category under which the template should be stored");
		setPageComplete(false);
	}

	public String category() {
		return category;
	}

	public Template template() {
		return template;
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
		var list = new List(body, SWT.BORDER);
		UI.gridData(list, true, true);
		var templates = Template.values();
		var items = new String[templates.length];
		for (int i = 0; i < templates.length; i++) {
			items[i] = templates[i].label();
		}
		list.setItems(items);

		Controls.onSelect(list, $ -> {
			int idx = list.getSelectionIndex();
			if (idx < 0) {
				template = null;
				setPageComplete(false);
			} else {
				template = templates[idx];
				setPageComplete(true);
			}
		});
	}
}
