package org.openlca.app.sh2e;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlca.app.editors.Editors;
import org.openlca.app.editors.SimpleEditorInput;
import org.openlca.app.editors.SimpleFormEditor;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.util.ErrorReporter;
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
			var header = createHeader(body, tk);
			var texts = TextBuilder.of(body, tk, header);

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

			var templateText = texts.add("""
					<p><b>Product system templates</b></p>
					<p>
						Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy
						eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam
						voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
						clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit.
						<a href="#">Click here to create a product system from a template.</a>
					</p>
					""");
			templateText.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {
					TemplateWizard.open();
				}
			});
		}

		private Label createHeader(Composite body, FormToolkit tk) {
			var comp = tk.createComposite(body);
			UI.gridLayout(comp, 1);
			UI.fillHorizontal(comp);
			var label = tk.createLabel(comp, "");
			label.setLayoutData(new GridData(
					SWT.CENTER, SWT.TOP, true, false));

			var banner = getClass().getResourceAsStream("banner.png");
			if (banner == null)
				return label;
			try (banner) {
				var image = new Image(body.getDisplay(), banner);
				body.addDisposeListener(e -> {
					if (!image.isDisposed()) {
						image.dispose();
					}
				});
				label.setImage(image);
			} catch (Exception e) {
				ErrorReporter.on("failed to create banner", e);
			}
			return label;
		}
	}


	private record TextBuilder(
			Composite body, FormToolkit tk, Font font, int width
	) {

		static TextBuilder of(Composite body, FormToolkit tk, Label header) {

			// construct the font
			var fontData = header.getFont().getFontData();
			for (var fd : fontData) {
				fd.setHeight(16);
			}
			var font = new Font(header.getDisplay(), fontData);
			body.addDisposeListener($ -> {
				if (!font.isDisposed()) {
					font.dispose();
				}
			});

			// get the width for the texts from the header image
			var image = header.getImage();
			int width = image != null
					? image.getImageData().width
					: 800;
			return new TextBuilder(body, tk, font, width);
		}

		FormText add(String text) {
			var comp = tk.createComposite(body);
			UI.gridLayout(comp, 1, 5, 5);
			UI.fillHorizontal(comp);
			var formText = tk.createFormText(comp, true);
			var textGrid = new GridData(SWT.CENTER, SWT.TOP, true, false);
			textGrid.widthHint = width;
			formText.setLayoutData(textGrid);
			formText.setFont(font);
			formText.setText("<html>" + text + "</html>", true, true);
			return formText;
		}
	}
}
