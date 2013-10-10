package org.openlca.app.editors.graphical.action;

import org.eclipse.jface.viewers.ISelection;
import org.openlca.app.Messages;
import org.openlca.app.editors.ModelEditorInput;
import org.openlca.app.editors.graphical.model.ProcessNode;
import org.openlca.app.processes.ProcessEditor;
import org.openlca.app.util.Editors;

public class OpenAction extends EditorAction {

	private ProcessNode node;

	OpenAction() {
		setId(ActionIds.OPEN);
		setText(Messages.OpenEditorAction_Text);
	}

	@Override
	protected boolean accept(ISelection selection) {
		node = getSingleSelectionOfType(selection, ProcessNode.class);
		return node != null;
	}

	@Override
	public void run() {
		Editors.open(new ModelEditorInput(node.getProcess()), ProcessEditor.ID);
	}

}
