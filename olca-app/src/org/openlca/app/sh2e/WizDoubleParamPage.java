package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.openlca.app.util.Colors;
import org.openlca.app.util.UI;

import java.util.function.Consumer;

public abstract class WizDoubleParamPage extends WizardPage {

	public Runnable checkState;

	protected WizDoubleParamPage(String pageName) {
		super(pageName);
	}

	protected Text labeledText(Composite body, Sh2e.Scope scope,
			Consumer<Sh2e.Option> setter) {
		var text = UI.labeledText(body, scope.label());
		UI.fillHorizontal(text);
		text.addModifyListener($ -> {
			eval(text, setter);
			checkState.run();
		});
		return text;
	}

	protected void eval(Text text, Consumer<Sh2e.Option> setter) {
		try {
			var amount = Double.parseDouble(text.getText());
			setter.accept(Sh2e.Option.from(String.valueOf(amount)));
			text.setToolTipText(null);
			text.setBackground(null);
		} catch (NumberFormatException e) {
			text.setToolTipText(text.getText() + " is not a valid number");
			text.setBackground(Colors.errorColor());
			setter.accept(null);
		}
	}

}
