package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.openlca.app.App;
import org.openlca.app.db.Cache;
import org.openlca.app.db.Database;
import org.openlca.app.navigation.Navigator;
import org.openlca.app.rcp.RcpActivator;
import org.openlca.app.sh2e.Sh2e.Boundaries;
import org.openlca.app.sh2e.Sh2e.FunctionalUnit;
import org.openlca.app.sh2e.Sh2e.Modelling;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Prospectivity;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.sh2e.Sh2e.UsePurpose;
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
	private WizScalingPage scalingPage;
	private WizBoundariesPage boundariesPage;
	private WizProductionPurposePage productionPurposePage;
	private WizProductionPage productionPage;
	private WizUsePurposePage usePurposePage;
	private WizTransportationPage transportationPage;
	private WizFuelChemicalPage fuelsChemicalPage;
	private WizComparativePage comparativePage;
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
	public boolean canFinish() {
		return getContainer().getCurrentPage() == templatePage
				&& templatePage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		var template = templatePage.template();
		if (template == null)
			return false;
		if (template.file() == null) {
			MsgBox.info(
					"Not yet available",
					"The selected template is not yet available");
			return false;
		}

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
		scalingPage = new WizScalingPage();
		boundariesPage = new WizBoundariesPage();
		productionPurposePage = new WizProductionPurposePage();
		productionPage = new WizProductionPage();
		usePurposePage = new WizUsePurposePage();
		transportationPage = new WizTransportationPage();
		fuelsChemicalPage = new WizFuelChemicalPage();
		comparativePage = new WizComparativePage();
		endOfLifePage = new WizEndOfLifePage();
		capitalGoodsPage = new WizCapitalGoodsPage();
		riskAssessmentPage = new WizRiskAssessmentPage();
		templatePage = new WizTemplatePage();
		addPage(startPage);
		addPage(modellingPage);
		addPage(prospectivityPage);
		addPage(technologyPage);
		addPage(scalingPage);
		addPage(boundariesPage);
		addPage(productionPurposePage);
		addPage(productionPage);
		addPage(usePurposePage);
		addPage(transportationPage);
		addPage(fuelsChemicalPage);
		addPage(comparativePage);
		addPage(endOfLifePage);
		addPage(capitalGoodsPage);
		addPage(riskAssessmentPage);
		addPage(templatePage);
	}

	private OptionSettings settings() {
		var settings = new OptionSettings();
		if (Option.No.equals(startPage.isDecisionSupport())) {
			settings.put(Scope.MODELLING, Modelling.ATTRIBUTIONAL);
		} else {
			settings.put(Scope.APPLICATION, modellingPage.application());
			settings.put(Scope.MODELLING, modellingPage.modelling());
		}

		settings.put(Scope.PROSPECTIVITY, prospectivityPage.prospectivity());
		if (prospectivityPage.prospectivity() == Prospectivity.PROSPECTIVE) {
			settings.put(Scope.TECHNOLOGY_LEVEL, technologyPage.technologyLevel());
			settings.put(Scope.OPERATING_SCALE, scalingPage.scale());
			settings.put(Scope.LEARNING_CURVE, scalingPage.learningCurve());
		}

		settings.put(Scope.BOUNDARIES, boundariesPage.boundaries());
		if (boundariesPage.boundaries() == Boundaries.PRODUCTION) {
			settings.put(Scope.USE_PURPOSE, UsePurpose.NONE);
		}
		if (boundariesPage.boundaries() == Boundaries.USE) {
			settings.put(Scope.USE_PURPOSE, Sh2e.ProductionPurpose.NONE);
		}
		if (boundariesPage.boundaries() != Boundaries.USE) {
			settings.put(Scope.PRODUCTION_PURPOSE, productionPurposePage.purpose());
			settings.put(Scope.HYDROGEN_CALORIFIC, productionPage.netCalorific());
			settings.put(Scope.HYDROGEN_PURITY, productionPage.purity());
			settings.put(Scope.HYDROGEN_TEMPERATURE, productionPage.temperature());
			settings.put(Scope.HYDROGEN_PRESSURE, productionPage.pressure());
			settings.put(Scope.HYDROGEN_CAPACITY, productionPage.capacity());
			settings.put(Scope.CCS, productionPage.ccs());
			settings.put(Scope.FUNCTIONAL_UNIT, productionPage.unit());
		}
		if (boundariesPage.boundaries() != Boundaries.PRODUCTION) {
			settings.put(Scope.USE_PURPOSE, usePurposePage.purpose());
			if (usePurposePage.purpose() == UsePurpose.TRANSPORTATION) {
				settings.put(Scope.VEHICLE_LIFETIME, transportationPage.lifetime());
				settings.put(Scope.VEHICLE_COMSUMPTION, transportationPage.consumption());
				settings.put(Scope.FUNCTIONAL_UNIT, transportationPage.unit());
			}
			if (usePurposePage.purpose() == UsePurpose.FUELS
					|| usePurposePage.purpose() == UsePurpose.CHEMICALS) {
				settings.put(Scope.FUEL_PURITY, fuelsChemicalPage.purity());
				settings.put(Scope.FUEL_PRESSURE, fuelsChemicalPage.pressure());
				settings.put(Scope.FUEL_TEMPERATURE, fuelsChemicalPage.temperature());
				settings.put(Scope.FUEL_CALORIFIC, fuelsChemicalPage.netCalorific());
				settings.put(Scope.FUNCTIONAL_UNIT, fuelsChemicalPage.unit());
			}
			if (usePurposePage.purpose() == UsePurpose.ELECTRICITY) {
				settings.put(Scope.FUNCTIONAL_UNIT, Sh2e.FunctionalUnit.MJ_ELEC);
			}
			if (usePurposePage.purpose() == UsePurpose.COGENERATION) {
				settings.put(Scope.FUNCTIONAL_UNIT, Sh2e.FunctionalUnit.MJ_COGENERATION);
			}

		}
		settings.put(Scope.COMPARATIVE_LCA, comparativePage.comparative());
		settings.put(Scope.END_OF_LIFE, endOfLifePage.endOfLife());
		settings.put(Scope.CAPITAL_GOODS, capitalGoodsPage.capitalGoods());
		settings.put(Scope.RISK_ASSESSMENT, riskAssessmentPage.riskAssessment());
		return settings;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == startPage) {
			if (Option.Yes.equals(startPage.isDecisionSupport())) {
				return modellingPage;
			} else {
				return prospectivityPage;
			}
		}
		if (page == modellingPage)
			return prospectivityPage;

		if (page == prospectivityPage) {
			if (prospectivityPage.prospectivity()
					== Prospectivity.RETROSPECTIVE)
				return boundariesPage;
			return technologyPage;
		}

		if (page == technologyPage)
			return scalingPage;

		if (page == scalingPage)
			return boundariesPage;

		if (page == boundariesPage) {
			templatePage.addFilter(boundariesPage.boundaries());
			return pageAfterBoundaries();
		}

		// Boundaries: PRODUCTION
		if (page == productionPurposePage) {
			templatePage.addFilter(productionPurposePage.purpose());
			return productionPage.withUse(
					boundariesPage.boundaries() == Boundaries.NONE);
		}
		// Boundaries: PRODUCTION or NONE
		if (page == productionPage) {
			templatePage.addFilter(productionPage.ccs(), productionPage.unit());
			if (boundariesPage.boundaries() == Boundaries.NONE) {
				return usePurposePage;
			}
			return comparativePage;
		}

		// Boundaries: USE
		if (page == usePurposePage) {
			templatePage.addFilter(usePurposePage.purpose());
			if (usePurposePage.purpose() == UsePurpose.TRANSPORTATION)
				return transportationPage;
			if (usePurposePage.purpose() == UsePurpose.FUELS)
				return fuelsChemicalPage.forFuel(true);
			if (usePurposePage.purpose() == UsePurpose.CHEMICALS)
				return fuelsChemicalPage.forFuel(false);
			if (usePurposePage.purpose() == UsePurpose.ELECTRICITY) {
				templatePage.addFilter(FunctionalUnit.MJ_ELEC);
				return comparativePage;
			}
			if (usePurposePage.purpose() == UsePurpose.COGENERATION) {
				templatePage.addFilter(FunctionalUnit.MJ_COGENERATION);
				return comparativePage;
			}
		}

		// Boundaries: USE or NONE
		if (page == transportationPage) {
			templatePage.addFilter(transportationPage.unit());
			return comparativePage;
		}
		if (page == fuelsChemicalPage) {
			templatePage.addFilter(fuelsChemicalPage.unit());
			return comparativePage;
		}

		if (page == comparativePage)
			return endOfLifePage;
		if (page == endOfLifePage)
			return capitalGoodsPage;
		if (page == capitalGoodsPage)
			return riskAssessmentPage;
		if (page == riskAssessmentPage) {
			templatePage.fillList();
			return templatePage;
		}

		return page instanceof WizTemplatePage
				? null
				: templatePage;
	}

	private IWizardPage pageAfterBoundaries() {
		if (boundariesPage.boundaries() == Boundaries.USE) {
			templatePage.addFilter(Sh2e.ProductionPurpose.NONE);
			return usePurposePage;
		} else {
			var prod = boundariesPage.boundaries() == Boundaries.PRODUCTION;
			if (prod) {
				templatePage.addFilter(Sh2e.UsePurpose.NONE);
				return productionPurposePage;
			} else {
				productionPurposePage.setPurpose(Sh2e.ProductionPurpose.PRODUCTION);
				templatePage.addFilter(Sh2e.ProductionPurpose.PRODUCTION);
				return productionPage.withUse(true);
			}
		}
	}

}
