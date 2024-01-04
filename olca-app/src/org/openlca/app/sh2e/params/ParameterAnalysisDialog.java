package org.openlca.app.sh2e.params;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.openlca.app.M;
import org.openlca.app.db.Database;
import org.openlca.app.util.MsgBox;
import org.openlca.app.util.UI;
import org.openlca.app.viewers.tables.Tables;

public class ParameterAnalysisDialog extends FormDialog {

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
		var systemCombo = UI.tableCombo(top, tk, SWT.BORDER | SWT.READ_ONLY);
		UI.fillHorizontal(systemCombo);


		UI.label(top, tk, M.ImpactAssessmentMethod);
		var methodCombo  = UI.tableCombo(top, tk, SWT.BORDER | SWT.READ_ONLY);
		UI.fillHorizontal(methodCombo);

		UI.label(top, tk, M.NumberOfIterations);
		var spinner = UI.spinner(top, tk, SWT.BORDER);
		spinner.setValues(10, 2, 100_000, 0, 1, 10);

		var table = Tables.createViewer(
				body, M.Parameter, M.Context, "Start value", "End value");
		Tables.bindColumnWidths(table, 0.3, 0.3, 0.2, 0.2);

	}


}
