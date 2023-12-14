package org.openlca.app.sh2e.slca.ui;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.openlca.app.rcp.images.Icon;
import org.openlca.app.results.ResultEditor;
import org.openlca.app.sh2e.slca.SocialRiskResult;
import org.openlca.app.util.Labels;
import org.openlca.app.util.UI;

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
	}
}
