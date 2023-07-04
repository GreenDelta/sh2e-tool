package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.openlca.app.App;
import org.openlca.app.db.Cache;
import org.openlca.app.db.Database;
import org.openlca.app.navigation.Navigator;
import org.openlca.app.rcp.RcpActivator;
import org.openlca.app.sh2e.Sh2e.Application;
import org.openlca.app.sh2e.Sh2e.Boundaries;
import org.openlca.app.sh2e.Sh2e.Modelling;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Prospectivity;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.ErrorReporter;
import org.openlca.app.util.MsgBox;
import org.openlca.app.util.UI;
import org.openlca.core.database.IDatabase;

class TemplateWizard extends Wizard {

	private final IDatabase db;

	private WizStartPage startPage;
	private WizModellingPage modellingPage;
	private WizProspectivityPage prospectivityPage;
	private WizTechnologyLevelPage technologyPage;
	private WizBoundariesPage boundariesPage;
	private WizCradleToGatePage cradleToGatePage;
	private WizEndOfLifePage endOfLifePage;
	private WizCapitalGoodsPage capitalGoodsPage;
	private WizRiskAssessmentPage riskAssessmentPage;
	private WizTemplatePage templatePage;

	private TemplateWizard(IDatabase db) {
		this.db = db;
		setNeedsProgressMonitor(true);
		setWindowTitle("FCH-LCA tool");
	}

	static void open() {
		var db = Database.get();
		if (db == null) {
			MsgBox.info(
					"Please create or open a database first",
					"Before you can start, you need to create " +
							"or open a database in the navigation panel " +
							"on the left side first.");
			return;
		}
		var wizard = new TemplateWizard(db);
		wizard.setDefaultPageImageDescriptor(
				RcpActivator.getImageDescriptor(
						"icons/immutable/sh2e_wizard.png"));
		var dialog = new WizardDialog(UI.shell(), wizard);
		dialog.open();
	}

	@Override
	public boolean performFinish() {
		var template = templatePage.template();
		if (template == null)
			return false;
		var category = templatePage.category();
		try {
			getContainer().run(true, false, monitor -> {
				var system = new TemplateImport(template, category, db)
						.run()
						.orElse(null);
				if (system == null)
					return;
				system.writeOtherProperties(settings().toJson());
				system = db.update(system);
				App.open(system);
			});
		} catch (Exception e) {
			ErrorReporter.on("Template import failed", e);
		} finally {
			Navigator.refresh();
			Cache.evictAll();
		}
		return true;
	}

	@Override
	public void addPages() {
		startPage = new WizStartPage();
		modellingPage = new WizModellingPage();
		prospectivityPage = new WizProspectivityPage();
		technologyPage = new WizTechnologyLevelPage();
		boundariesPage = new WizBoundariesPage();
		cradleToGatePage = new WizCradleToGatePage();
		endOfLifePage = new WizEndOfLifePage();
		capitalGoodsPage = new WizCapitalGoodsPage();
		riskAssessmentPage = new WizRiskAssessmentPage();
		templatePage = new WizTemplatePage();
		addPage(startPage);
		addPage(modellingPage);
		addPage(prospectivityPage);
		addPage(technologyPage);
		addPage(boundariesPage);
		addPage(cradleToGatePage);
		addPage(endOfLifePage);
		addPage(capitalGoodsPage);
		addPage(riskAssessmentPage);
		addPage(templatePage);
	}

	private OptionSettings settings() {
		var settings = new OptionSettings();
		if (Option.No.equals(startPage.isDecisionSupport())) {
			settings.put(Scope.APPLICATION, Application.ACCOUNTING);
			settings.put(Scope.MODELLING, Modelling.ATTRIBUTIONAL);
		} else {
			settings.put(Scope.APPLICATION, modellingPage.application());
			settings.put(Scope.MODELLING, modellingPage.modelling());
		}

		settings.put(Scope.PROSPECTIVITY, prospectivityPage.prospectivity());
		settings.put(Scope.TECHNOLOGY_LEVEL, technologyPage.technologyLevel());
		settings.put(Scope.BOUNDARIES, boundariesPage.boundaries());
		settings.put(Scope.CRADLE_TO_GATE, cradleToGatePage.cradleToGate());
		settings.put(Scope.END_OF_LIFE, endOfLifePage.endOfLife());
		settings.put(Scope.CAPITAL_GOODS, capitalGoodsPage.capitalGoods());
		settings.put(Scope.RISK_ASSESSMENT, riskAssessmentPage.riskAssessment());
		return settings;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == startPage) {
			if (Option.Yes.equals(startPage.isDecisionSupport())) {
				modellingPage.setPageComplete(false);
				return modellingPage;
			} else {
				modellingPage.setPageComplete(true);
				return prospectivityPage;
			}
		}
		if (page == modellingPage)
			return prospectivityPage;

		if (page == prospectivityPage) {
			if (prospectivityPage.prospectivity() == Prospectivity.RETROSPECTIVE) {
				technologyPage.setPageComplete(true);
				return boundariesPage;
			} else {
				technologyPage.setPageComplete(
						technologyPage.technologyLevel() != null);
				return technologyPage;
			}
		}

		if (page == technologyPage)
			return boundariesPage;

		if (page == boundariesPage) {
			if (boundariesPage.boundaries() == Boundaries.USE) {
				cradleToGatePage.setPageComplete(true);
				return endOfLifePage;
			} else {
				cradleToGatePage.setPageComplete(
						cradleToGatePage.cradleToGate() != null);
				return cradleToGatePage;
			}
		}

		if (page == cradleToGatePage)
			return endOfLifePage;
		if (page == endOfLifePage)
			return capitalGoodsPage;
		if (page == capitalGoodsPage)
			return riskAssessmentPage;
		if (page == riskAssessmentPage)
			return templatePage;

		return page instanceof WizTemplatePage
				? null
				: templatePage;
	}
}
