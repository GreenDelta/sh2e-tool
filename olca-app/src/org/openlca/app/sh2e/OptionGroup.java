package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.util.Controls;
import org.openlca.app.util.UI;

class OptionGroup {

	private final Sh2e.Group group;
	private Sh2e.Option selected;

	private OptionGroup(Sh2e.Group group) {
		this.group = group;
		selected = group.options().get(0);
	}

	static OptionGroup of(Sh2e.Group group) {
		return new OptionGroup(group);
	}

	Option selected() {
		return selected;
	}

	void renderOn(Composite comp) {
		var widget = new Group(comp, SWT.NONE);
		UI.fillHorizontal(widget);
		widget.setText(group.label());
		UI.gridLayout(widget, 1);

		for (var opt : group.options()) {
			var btn = new Button(widget, SWT.RADIO);
			btn.setSelection(opt.equals(selected));
			btn.setText(opt.label());
			Controls.onSelect(btn, e -> selected = opt);
		}
	}
}
