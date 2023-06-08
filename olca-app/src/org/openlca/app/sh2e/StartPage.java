package org.openlca.app.sh2e;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlca.app.editors.Editors;
import org.openlca.app.editors.SimpleEditorInput;
import org.openlca.app.editors.SimpleFormEditor;
import org.openlca.app.rcp.images.Icon;
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

			var texts = new TextBuilder(
					body, tk, image.getImageData().width);
			texts.add(
					"""
							<p><b>Welcome to the sh2e-tool</b></p>
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
							""".strip());

			texts.add("""
					<p><b>Product system templates</b></p>
					<p>
						Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy
						eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam
						voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
						clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit
						<a href="#">Click here to create</a>
					</p>
					""");

		}

	}

	private record TextBuilder(Composite body, FormToolkit tk, int width) {

		FormText add(String text) {
			var comp = tk.createComposite(body);
			UI.gridLayout(comp, 1, 5, 5);
			UI.fillHorizontal(comp);
			var formText = tk.createFormText(comp, true);
			var textGrid = new GridData(SWT.CENTER, SWT.TOP, true, false);
			textGrid.widthHint = width;
			formText.setLayoutData(textGrid);
			formText.setText("<html>" + text + "</html>", true, true);
			return formText;
		}

	}
}
