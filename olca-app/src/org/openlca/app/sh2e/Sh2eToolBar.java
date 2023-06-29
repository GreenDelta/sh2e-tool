package org.openlca.app.sh2e;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.openlca.app.rcp.RcpActivator;
import org.openlca.app.util.Actions;

public class Sh2eToolBar extends EditorActionBarContributor {

	private static final Action action = Actions.create(
			"Open the FCH-LCA tool wizard",
			RcpActivator.getImageDescriptor("icons/immutable/sh2e_icon.png"),
			TemplateWizard::open);

	@Override
	public void contributeToToolBar(IToolBarManager toolbar) {
		toolbar.add(action);
	}

	public static Action getAction() {
		return action;
	}
}
