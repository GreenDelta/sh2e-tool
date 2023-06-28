package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.openlca.app.sh2e.Sh2e.CapitalGoods;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;
import org.openlca.util.Strings;

class WizCapitalGoodsPage extends WizardPage {

	private Option selected;

	WizCapitalGoodsPage() {
		super("WizCapitalGoodsPage");
		setTitle("Capital goods");
		setPageComplete(false);
	}

	Option capitalGoods() {
		return selected;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		var group = OptionGroup.of(
				Scope.CAPITAL_GOODS.label(),
				"Are there any excluded capital goods in any phase?",
				Option.No,
				Option.other("Yes. Please state and include a justification"));
		var comp = group.renderOn(body).widget();
		var text = UI.text(comp);
		UI.fillHorizontal(text);
		text.setEnabled(false);
		text.addModifyListener($ -> eval(text));

		group.onSelect(option -> {
			if (Option.No.equals(option)) {
				selected = CapitalGoods.INCLUDED;
				setPageComplete(true);
			} else {
				text.setEnabled(true);
				eval(text);
			}
		});
	}

	private void eval(Text text) {
		var msg = text.getText().strip();
		if (Strings.notEmpty(msg)) {
			selected = Option.other(msg);
			setPageComplete(true);
		} else {
			selected = null;
			setPageComplete(false);
		}
	}
}
