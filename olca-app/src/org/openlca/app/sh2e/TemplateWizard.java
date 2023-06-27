package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Application;
import org.openlca.app.sh2e.Sh2e.Modelling;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

import java.util.EnumMap;

class TemplateWizard extends Wizard {

	final EnumMap<Scope, Option> selection = new EnumMap<>(Scope.class);

	private StartPage startPage;
	private WizModellingPage modellingPage;
	private WizProspectivityPage prospectivityPage;
	private WizBoundariesPage boundariesPage;
	private WizTemplatePage templatePage;

	private TemplateWizard() {
		setNeedsProgressMonitor(true);
		setWindowTitle("FCH-LCA tool");
	}

	static void open() {
		var wizard = new TemplateWizard();
		var dialog = new WizardDialog(UI.shell(), wizard);
		// dialog.setPageSize(250, 250);
		dialog.open();
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	@Override
	public void addPages() {
		startPage = new StartPage(this);
		modellingPage = new WizModellingPage(this);
		prospectivityPage = new WizProspectivityPage();
		boundariesPage = new WizBoundariesPage();
		templatePage = new WizTemplatePage(this);
		addPage(startPage);
		addPage(modellingPage);
		addPage(prospectivityPage);
		addPage(boundariesPage);
		addPage(templatePage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (!selection.containsKey(Scope.MODELLING))
			return modellingPage;
		if (page == modellingPage)
			return prospectivityPage;
		if (page == prospectivityPage)
			return boundariesPage;
		if (page == boundariesPage)
			return templatePage;

		return page instanceof WizTemplatePage
				? null
				: templatePage;
	}

	private static class StartPage extends WizardPage {

		private final TemplateWizard wizard;

		StartPage(TemplateWizard wizard) {
			super("StartPage");
			this.wizard = wizard;
			setTitle("Welcome to the SH2E tool");
			setDescription("To get through this dialog, " +
					"it is recommended that you refer to the " +
					"SH2E guidelines for clarification.");
			setPageComplete(false);
		}

		@Override
		public IWizardPage getNextPage() {
			return wizard.getNextPage(this);
		}

		@Override
		public void createControl(Composite parent) {
			var body = UI.composite(parent);
			UI.gridLayout(body, 1);
			setControl(body);

			var group = OptionGroup.of(
					"Goal & scope",
					"Is the application of the LCA " +
							"intended for decision support?",
					Option.Yes, Option.No
			);
			group.renderOn(body).onSelect(option -> {
				setPageComplete(true);
				if (option.equals(Option.No)) {
					wizard.selection.put(
							Scope.APPLICATION, Application.ACCOUNTING);
					wizard.selection.put(
							Scope.MODELLING, Modelling.ATTRIBUTIONAL);
				} else {
					wizard.selection.remove(Scope.APPLICATION);
					wizard.selection.remove(Scope.MODELLING);
				}
			});
		}
	}
}
