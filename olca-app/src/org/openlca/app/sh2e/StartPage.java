package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.openlca.app.editors.Editors;
import org.openlca.app.editors.SimpleEditorInput;
import org.openlca.app.editors.SimpleFormEditor;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.util.Colors;
import org.openlca.app.util.UI;

public class StartPage extends SimpleFormEditor {

	public static void open() {
		var input = new SimpleEditorInput("sh2e.StartPage", "Welcome");
		Editors.open(input, "sh2e.StartPage");
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		setTitleImage(Icon.HOME.get());
	}

	@Override
	protected FormPage getPage() {
		return new Page();
	}

	private class Page extends FormPage {

		Page() {
			super(StartPage.this, "sh2e.StartPage.Page", "Welcome");
		}

		@Override
		protected void createFormContent(IManagedForm mForm) {
			var form = mForm.getForm();
			var tk = mForm.getToolkit();
			var body = UI.body(form, tk);

			var image = new Image(
					form.getDisplay(),
					getClass().getResourceAsStream("banner.png"));
			form.addDisposeListener(e -> {
				if (!image.isDisposed()) {
					image.dispose();
				}
			});

			var imgComp = tk.createComposite(body);
			UI.gridLayout(imgComp, 1);
			UI.fillHorizontal(imgComp);
			var imgLabel = tk.createLabel(imgComp, "");
			imgLabel.setImage(image);

			imgLabel.setLayoutData(new GridData(
					SWT.CENTER,
					SWT.TOP,
					true,
					false));

			var comp = tk.createComposite(body);
			// comp.setLayoutData(new GridData(
			//		SWT.CENTER,
			//		SWT.TOP,
			//		true,
			//		false));
			UI.gridLayout(comp, 1);
			UI.fillHorizontal(comp);
			comp.setBackground(Colors.errorColor());

			var text = tk.createFormText(comp, true);
			var textGrid = new GridData(SWT.CENTER, SWT.TOP, true, false);
			textGrid.widthHint = image.getImageData().width;
			text.setLayoutData(textGrid);

			//UI.gridData(text, false, false).widthHint =  image.getImageData().width;

			text.setText(
					"""
			        <html>
							<h1>Welcome to the sh2e-tool</h1>
							<p>
							Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy
							eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam
							voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
							clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit
							amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam
							nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,
							sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum.
							Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor
							sit amet.
							</p>
							</html>
							""".strip()
					, true, true);

		}
	}
}
