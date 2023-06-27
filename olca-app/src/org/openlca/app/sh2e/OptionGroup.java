package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.Controls;
import org.openlca.app.util.UI;

import java.util.List;
import java.util.function.Consumer;

class OptionGroup {

	private final String scope;
	private final String question;
	private final List<Option> options;
//	private Option selected;

	private Consumer<Option> listener;

	private OptionGroup(
			String scope, String question, List<Option> options
	) {
		this.scope = scope;
		this.question = question;
		this.options = options;
	//	selected = options.isEmpty() ? null : options.get(0);
	}

	static OptionGroup of(Scope group, String question) {
		return new OptionGroup(
				group.label(), question, group.options());
	}

	static OptionGroup of(
			String title, String question, Option... options
	) {
		return new OptionGroup(
				title, question, List.of(options));
	}

	void onSelect(Consumer<Option> fn) {
		this.listener = fn;
	}
//	Option selected() {
//		return selected;
//	}

	OptionGroup renderOn(Composite comp) {
		var group = new Group(comp, SWT.NONE);
		UI.fillHorizontal(group);
		group.setText(scope);
		UI.gridLayout(group, 1);
		UI.label(group, question);
		for (var opt : options) {
			var btn = new Button(group, SWT.RADIO);
			btn.setSelection(false);
			btn.setText(opt.label());
			Controls.onSelect(btn, e -> {
				if (listener != null) {
					listener.accept(opt);
				}
			});
		}
		return this;
	}
}
