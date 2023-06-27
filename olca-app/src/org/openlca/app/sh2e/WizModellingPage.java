package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Application;
import org.openlca.app.sh2e.Sh2e.Modelling;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class WizModellingPage extends WizardPage  {

	private final TemplateWizard wizard;

	WizModellingPage(TemplateWizard wizard) {
		super("WizModellingPage");
		this.wizard = wizard;
		setTitle("Modelling principles");
		setPageComplete(true);
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var scaleGroup = OptionGroup.of(
				"Scale",
				"What is the scale of the decision support?",
				Application.MICRO, Application.MACRO);
		scaleGroup.renderOn(body).onSelect(option ->
				wizard.selection.put(Scope.APPLICATION, option));

		var statusGroup = OptionGroup.of(
				"Status quo",
				"Will the status quo change and can " +
						"this change be modelled with a net benefit?",
				Option.Yes, Option.No);
		statusGroup.renderOn(body).onSelect(option -> {
			if (option.equals(Option.Yes)) {
				wizard.selection.put(Scope.MODELLING, Modelling.CONSEQUENTIAL);
			} else {
				wizard.selection.put(Scope.MODELLING, Modelling.ATTRIBUTIONAL);
			}
		});
	}
}
