package org.openlca.app.tools.mapping;

import java.io.File;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.openlca.app.components.FileChooser;
import org.openlca.app.editors.Editors;
import org.openlca.app.tools.mapping.model.IMapProvider;
import org.openlca.app.util.Actions;
import org.openlca.app.util.Error;
import org.openlca.app.util.Info;

public class MappingMenu extends EditorActionBarContributor {

	@Override
	public void contributeToMenu(IMenuManager root) {
		MenuManager menu = new MenuManager("Flow mapping");
		root.add(menu);
		menu.add(Actions.onOpen(this::onOpen));
	}

	private void onOpen() {
		File file = FileChooser.forImport("*.zip");
		if (file == null)
			return;
		MappingTool tool = Editors.getActive();
		if (tool == null)
			return;
		try {
			IMapProvider.Type type = IMapProvider.Type.of(file);
			if (type == IMapProvider.Type.JSON_LD_PACKAGE) {
				JsonImportDialog.open(file, tool);
			} else {
				Info.showBox("#Unsupported format.");
			}
		} catch (Exception e) {
			Error.showBox("Could not open file", e.getMessage());
		}
	}

}