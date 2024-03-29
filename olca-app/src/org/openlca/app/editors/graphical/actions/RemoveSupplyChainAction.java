package org.openlca.app.editors.graphical.actions;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.openlca.app.M;
import org.openlca.app.editors.graphical.GraphEditor;
import org.openlca.app.editors.graphical.edit.ExchangeEditPart;
import org.openlca.app.editors.graphical.edit.NodeEditPart;
import org.openlca.app.editors.graphical.model.GraphLink;
import org.openlca.app.rcp.images.Icon;
import org.openlca.core.model.FlowType;
import org.openlca.core.model.ProcessLink;

import java.util.*;

import static org.openlca.app.editors.graphical.requests.GraphRequestConstants.REQ_REMOVE_CHAIN;

public class RemoveSupplyChainAction extends SelectionAction {

	public static final String KEY_LINKS = "links";
	private final GraphEditor editor;

	public RemoveSupplyChainAction(GraphEditor part) {
		super(part);
		editor = part;
		setId(GraphActionIds.REMOVE_SUPPLY_CHAIN);
		setImageDescriptor(Icon.REMOVE_SUPPLY_CHAIN.descriptor());
	}

	@Override
	protected boolean calculateEnabled() {
		var command = getCommand();
		return command != null && command.canExecute();
	}

	@Override
	public void run() {
		execute(getCommand());
	}

	private Command getCommand() {
		if (getSelectedObjects().isEmpty())
			return null;

		var viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(
				GraphicalViewer.class);
		var registry = viewer.getEditPartRegistry();
		var graphEditPart = (EditPart) registry.get(editor.getModel());
		if (graphEditPart == null)
			return null;

		var links = new ArrayList<ProcessLink>();


		for (var object : getSelectedObjects()) {
			addContributor(object, links);
		}

		if (!links.isEmpty()) {
			var request = new Request(REQ_REMOVE_CHAIN);
			var data = new HashMap<String, Object>();
			data.put(KEY_LINKS, links);
			request.setExtendedData(data);
			return graphEditPart.getCommand(request);
		} else return null;
	}

	private void addContributor(Object object, List<ProcessLink> links) {
		var linkSearch = editor.getModel().linkSearch;
		if (NodeEditPart.class.isAssignableFrom(object.getClass())) {
			setText(M.RemoveSupplyChain);
			var nodeId = ((NodeEditPart) object).getModel().descriptor.id;
			links.addAll(linkSearch.getConnectionLinks(nodeId));
		}
		else if (object instanceof ExchangeEditPart part) {
			setText(M.RemoveFlowSupplyChain);
			var e = part.getModel().exchange;
			if ((e.flow.flowType == FlowType.WASTE_FLOW && !e.isInput)
					|| (e.flow.flowType == FlowType.PRODUCT_FLOW && e.isInput)) {
				var connection = part.getModel().getAllConnections();
				// there should only one link to a waste output or product input.
				if (connection.size() != 1)
					return;
				if (connection.get(0) instanceof GraphLink link)
					links.add(link.processLink);
			}
		}
	}


}
