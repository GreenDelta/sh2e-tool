package org.openlca.app.sh2e.params;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlca.app.M;
import org.openlca.app.components.ParameterRedefDialog;
import org.openlca.app.db.Database;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.rcp.images.Images;
import org.openlca.app.util.Actions;
import org.openlca.app.util.Labels;
import org.openlca.app.util.MsgBox;
import org.openlca.app.util.UI;
import org.openlca.app.viewers.Viewers;
import org.openlca.app.viewers.tables.Tables;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.ImpactCategory;
import org.openlca.core.model.ImpactMethod;
import org.openlca.core.model.ModelType;
import org.openlca.core.model.ParameterRedef;
import org.openlca.core.model.Process;
import org.openlca.core.model.ProductSystem;
import org.openlca.core.model.descriptors.Descriptor;
import org.openlca.util.Strings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ParameterAnalysisDialog extends FormDialog {

	private final IDatabase db = Database.get();
	private final List<Param> params = new ArrayList<>();
	private TableComboViewer systemCombo;
	private TableComboViewer methodCombo;
	private Spinner iterationSpinner;
	private TableViewer paramTable;

	public static void show() {
		if (Database.get() == null) {
			MsgBox.info(M.NoDatabaseOpened, M.NeedOpenDatabase);
			return;
		}
		new ParameterAnalysisDialog().open();
	}

	private ParameterAnalysisDialog() {
		super(UI.shell());
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Parameter analysis");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	@Override
	protected void createFormContent(IManagedForm form) {
		var tk = form.getToolkit();
		var body = UI.dialogBody(form.getForm(), tk);

		var top = tk.createComposite(body);
		UI.fillHorizontal(top);
		UI.gridLayout(top, 2);

		UI.label(top, tk, M.ProductSystem);
		systemCombo = createCombo(top, tk, db.getDescriptors(ProductSystem.class));

		UI.label(top, tk, M.ImpactAssessmentMethod);
		methodCombo = createCombo(top, tk, db.getDescriptors(ImpactMethod.class));

		UI.label(top, tk, M.NumberOfIterations);
		iterationSpinner = UI.spinner(top, tk, SWT.BORDER);
		iterationSpinner.setValues(10, 2, 100_000, 0, 1, 10);

		paramTable = Tables.createViewer(
				body, M.Parameter, M.Context, "Start value", "End value");
		paramTable.setLabelProvider(new ParamLabel());
		Tables.bindColumnWidths(paramTable, 0.3, 0.3, 0.2, 0.2);
		var onAdd = Actions.onAdd(this::addParams);
		Actions.bind(paramTable, onAdd);
		paramTable.setInput(params);
	}

	private TableComboViewer createCombo(
			Composite comp, FormToolkit tk, List<? extends Descriptor> descriptors
	) {
		var combo = UI.tableCombo(comp, tk, SWT.BORDER | SWT.READ_ONLY);
		UI.fillHorizontal(combo);
		var viewer = new TableComboViewer(combo);
		viewer.setLabelProvider(new ComboLabel());
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object o1, Object o2) {
				if (!(o1 instanceof Descriptor d1) || !(o2 instanceof Descriptor d2))
					return super.compare(viewer, o1, o2);
				var n1 = Labels.name(d1);
				var n2 = Labels.name(d2);
				return Strings.compare(n1, n2);
			}
		});
		viewer.setInput(descriptors);
		if (!descriptors.isEmpty()) {
			viewer.setSelection(new StructuredSelection(descriptors.get(0)));
		}
		return viewer;
	}

	private void addParams() {
		var contexts = new HashSet<Long>(1);
		var obj = Viewers.getFirstSelected(systemCombo);
		if (obj instanceof Descriptor d) {
			contexts.add(d.id);
		}
		var redefs = ParameterRedefDialog.select(contexts);
		var added = false;
		for (var redef : redefs) {
			var param = params.stream()
					.filter(p -> p.hasRedef(redef))
					.findAny()
					.orElse(null);
			if (param != null)
				continue;
			added = true;
			param = Param.of(redef, db);
			params.add(param);
		}
		if (added) {
			paramTable.setInput(params);
		}
	}

	private static class ComboLabel extends BaseLabelProvider
			implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object obj, int col) {
			return col == 0 && obj instanceof Descriptor d
					? Images.get(d)
					: null;
		}

		@Override
		public String getColumnText(Object obj, int col) {
			return col == 0 && obj instanceof Descriptor d
					? Labels.name(d)
					: null;
		}
	}

	private static class ParamLabel extends BaseLabelProvider
			implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object obj, int col) {
			if (!(obj instanceof Param param))
				return null;
			if (col == 0)
				return Icon.FORMULA.get();
			if (col == 1 && param.context != null)
				return Images.get(param.context);
			return null;
		}

		@Override
		public String getColumnText(Object obj, int col) {
			if (!(obj instanceof Param param))
				return null;
			return switch (col) {
				case 0 -> param.redef.name;
				case 1 -> param.context != null
						? Labels.name(param.context)
						: "global";
				case 2 -> Double.toString(param.start);
				case 3 -> Double.toString(param.end);
				default -> null;
			};
		}
	}

	record Param(
			ParameterRedef redef,
			Descriptor context,
			double start,
			double end) {

		static Param of(ParameterRedef redef, IDatabase db) {
			Descriptor context = null;
			if (redef.contextId != null) {
				context = redef.contextType == ModelType.IMPACT_CATEGORY
						? db.getDescriptor(ImpactCategory.class, redef.contextId)
						: db.getDescriptor(Process.class, redef.contextId);
			}
			return new Param(redef, context, redef.value, redef.value);
		}

		boolean hasRedef(ParameterRedef r) {
			if (redef == null || r == null)
				return false;
			return Strings.nullOrEqual(redef.name, r.name)
					&& Objects.equals(redef.contextId, r.contextId);
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;
			if (o == this)
				return true;
			if (!(o instanceof Param other))
				return false;
			return this.hasRedef(other.redef);
		}
	}
}
