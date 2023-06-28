package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.openlca.app.sh2e.Sh2e.Application;
import org.openlca.app.sh2e.Sh2e.Modelling;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class TemplateWizard extends Wizard {

	private WizStartPage startPage;
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
		startPage = new WizStartPage();
		modellingPage = new WizModellingPage();
		prospectivityPage = new WizProspectivityPage();
		boundariesPage = new WizBoundariesPage();
		templatePage = new WizTemplatePage(this);
		addPage(startPage);
		addPage(modellingPage);
		addPage(prospectivityPage);
		addPage(boundariesPage);
		addPage(templatePage);
	}

	OptionSettings settings() {
		var settings = new OptionSettings();

		if (Option.No.equals(startPage.isDecisionSupport())) {
			settings.put(Scope.APPLICATION, Application.ACCOUNTING);
			settings.put(Scope.MODELLING, Modelling.ATTRIBUTIONAL);
		} else {
			settings.put(Scope.APPLICATION, modellingPage.application());
			settings.put(Scope.MODELLING, modellingPage.modelling());
		}

		return settings;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof WizStartPage sp) {
			return Option.Yes.equals(sp.isDecisionSupport())
					? modellingPage
					: boundariesPage;
		}

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

}
