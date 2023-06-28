package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;

class WizRiskAssessmentPage extends WizardPage {

	private Option selected;

	WizRiskAssessmentPage() {
		super("WizRiskAssessmentPage");
		setTitle("Risk assessment");
		setPageComplete(false);
	}

	Option riskAssessment() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);
		OptionGroup.of(
				Scope.RISK_ASSESSMENT,
				"Please specify if risk assessment is included"	)
				.renderOn(body)
				.onSelect(option -> {
					setPageComplete(true);
					selected = option;
				});
	}
}
