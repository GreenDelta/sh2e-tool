package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.app.util.UI;
import org.openlca.core.model.ProductSystem;


public class SH2EditorSection {

	private final ProductSystem system;

	private SH2EditorSection(ProductSystem system) {
		this.system = system;
	}

	public static SH2EditorSection of(ProductSystem system) {
		return new SH2EditorSection(system);
	}

	public void renderOn(Composite body, FormToolkit tk) {
		var comp = UI.formSection(body, tk, "FCH-LCA Properties");
		for (var scope : Scope.values()) {
			UI.labeledText(comp, tk, scope.label(), SWT.READ_ONLY);
		}
	}


}
