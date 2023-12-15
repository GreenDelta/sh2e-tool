package org.openlca.app.sh2e.slca.ui;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.results.ResultEditor;
import org.openlca.app.sh2e.slca.SocialRiskResult;
import org.openlca.app.sh2e.slca.ui.TreeModel.Node;
import org.openlca.app.util.Labels;
import org.openlca.app.util.Numbers;
import org.openlca.app.util.UI;
import org.openlca.app.viewers.trees.Trees;
import org.openlca.core.model.RiskLevel;

public class SocialResultPage extends FormPage {

	private final ResultEditor editor;
	private final SocialRiskResult result;

	public SocialResultPage(ResultEditor editor, SocialRiskResult result) {
		super(editor, "SocialResultPage", "Social assessment");
		this.editor = editor;
		this.result = result;
	}

	@Override
	protected void createFormContent(IManagedForm mForm) {
		var form = UI.header(mForm,
				Labels.name(editor.setup.target()),
				Icon.ANALYSIS_RESULT.get());
		var tk = mForm.getToolkit();
		var body = UI.body(form, tk);
		var section = UI.section(body, tk, "Indicator results");
		var comp = UI.sectionClient(section, tk, 1);
		UI.gridData(section, true, true);

		var levels = RiskLevel.values();
		var headers = new String[2 + levels.length];
		headers[0] = "";
		headers[1] = "Activity variable";
		for (var rl : levels) {
			int col = TreeGrid.columnOf(rl);
			if (col < 0 || col >= headers.length)
				continue;
			headers[col] = TreeGrid.headerOf(rl);
		}
		var tree = Trees.createViewer(comp, headers);

		double[] widths = new double[headers.length];
		widths[0] = 0.3;
		widths[1] = 0.2;
		for (int i = 2; i < widths.length; i++) {
			widths[i] = 0.5 / levels.length;
		}
		Trees.bindColumnWidths(tree.getTree(), widths);

		for (var level : levels) {
			int col = TreeGrid.columnOf(level);
			var t = tree.getTree();
			if (col < 0 || col >= t.getColumnCount())
				continue;
			t.getColumn(col).setToolTipText(Labels.of(level));
		}

		tree.setLabelProvider(new TreeLabel());
		var model = new TreeModel(result);
		tree.setContentProvider(model);
		tree.setInput(model);
	}

	private static class TreeLabel extends BaseLabelProvider
			implements ITableLabelProvider, ITableColorProvider {

		@Override
		public Image getColumnImage(Object obj, int col) {
			if (!(obj instanceof Node n))
				return null;
			return col == 0 ? n.icon() : null;
		}

		@Override
		public String getColumnText(Object obj, int col) {
			if (!(obj instanceof Node n))
				return null;
			return switch (col) {
				case 0 -> n.name();
				case 1 -> n.variable();
				default -> {
					var level = TreeGrid.levelOf(col);
					if (level == null)
						yield null;
					var value = n.value().get(level);
					yield Numbers.format(value, 2);
				}
			};
		}

		@Override
		public Color getForeground(Object obj, int col) {
			return null;
		}

		@Override
		public Color getBackground(Object obj, int col) {
			if (col < 2 || !(obj instanceof Node n))
				return null;
			var level = TreeGrid.levelOf(col);
			if (level == null)
				return null;
			var share = n.value().getShare(level);
			return share >= 0.75
					? TreeGrid.colorOf(level)
					: null;
		}
	}
}
