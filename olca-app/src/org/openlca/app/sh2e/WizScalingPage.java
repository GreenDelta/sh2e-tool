package org.openlca.app.sh2e;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.UI;
import org.openlca.util.Strings;

class WizScalingPage extends WizardPage {

	private Option operatingScale;
	private Option learningCurve;

	WizScalingPage() {
		super("WizScalingPage");
		setTitle("Scaling and learning curves");
		setPageComplete(false);
	}

	Option scale() {
		return operatingScale;
	}

	Option learningCurve() {
		return learningCurve;
	}

	@Override
	public void createControl(Composite parent) {
		var body = UI.composite(parent);
		UI.gridLayout(body, 1);
		setControl(body);

		Runnable checkState = () -> setPageComplete(
				operatingScale != null && learningCurve != null);

		var scaleGroup = OptionGroup.of(
				Sh2e.Scope.OPERATING_SCALE,
				"Is the life cycle inventory considered based on the operating scale?");
		scaleGroup.renderOn(body).onSelect(option -> {
			operatingScale = option;
			checkState.run();
		});

		var learningCurveGroup = OptionGroup.of(
				Sh2e.Scope.LEARNING_CURVE,
				"Have you accounted for learning curve phenomena?");
		var comp = learningCurveGroup.renderOn(body).widget();
		var text = UI.text(comp);
		UI.fillHorizontal(text);
		text.setEnabled(false);
		text.addModifyListener($ -> {
			eval(text);
			checkState.run();
		});

		learningCurveGroup.onSelect(option -> {
			if (Sh2e.LearningCurve.NOT_STATED.equals(option)) {
				learningCurve = option;
				text.setText("");
				setPageComplete(true);
				text.setEnabled(false);
			} else {
				text.setEnabled(true);
				eval(text);
				checkState.run();
			}
		});
	}

	private void eval(Text text) {
		var msg = text.getText().strip();
		learningCurve =  Strings.notEmpty(msg)
				? Option.from(msg) : null;
	}

}
