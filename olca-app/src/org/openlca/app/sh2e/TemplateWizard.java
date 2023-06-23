package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openlca.app.util.UI;

import java.util.List;

public class TemplateWizard extends Wizard {

	private TemplateWizard() {
		setNeedsProgressMonitor(true);
		setWindowTitle("Create an H2 product system...");
	}

	public static void open() {
		var wizard = new TemplateWizard();
		var dialog = new WizardDialog(UI.shell(), wizard);
		dialog.open();
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	@Override
	public void addPages() {
		addPage(new Page());
	}

	private static class Page extends WizardPage {

		Page() {
			super("TemplateWizard.Page");
			setTitle("Select the properties of the system");
			setPageComplete(true);
		}

		@Override
		public void createControl(Composite parent) {
			var body = UI.composite(parent);
			UI.gridLayout(body, 1);
			setControl(body);

			var group = new Group(body, SWT.NONE);
			UI.fillHorizontal(group);
			UI.gridLayout(group, 1);
			group.setText("Intended application");

			var options = List.of(
					"Micro-level decision support",
					"Meso/Macro-level decision support",
					"Accounting");
			for (var opt : options) {
				var btn = new Button(group, SWT.RADIO);
				btn.setText(opt);
			}
		}
	}

}
