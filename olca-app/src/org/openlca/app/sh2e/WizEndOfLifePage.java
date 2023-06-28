package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.openlca.app.sh2e.Sh2e.EndOfLife;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;
import org.openlca.util.Strings;

class WizEndOfLifePage extends WizardPage {

	private Option selected;

	WizEndOfLifePage() {
		super("WizEndOfLifePage");
		setTitle("End-of-life");
		setPageComplete(false);
	}

	Option endOfLife() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var group = OptionGroup.of(
				Scope.END_OF_LIFE,
				"Please select the choice of end-of-life modelling approach");
		var comp = group.renderOn(body).widget();
		var text = UI.text(comp);
		UI.fillHorizontal(text);
		text.setEnabled(false);
		text.addModifyListener($ -> eval(text));

		group.onSelect(option -> {
			if (option == EndOfLife.OTHER) {
				text.setEnabled(true);
				eval(text);
			} else {
				selected = option;
				text.setEnabled(false);
				setPageComplete(true);
			}
		});
	}

	private void eval(Text text) {
		var msg = text.getText().strip();
		if (Strings.notEmpty(msg)) {
			selected = Option.from(msg);
			setPageComplete(true);
		} else {
			selected = null;
			setPageComplete(false);
		}
	}

}
