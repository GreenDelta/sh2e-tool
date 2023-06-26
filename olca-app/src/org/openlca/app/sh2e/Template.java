package org.openlca.app.sh2e;

enum Template {

	T1("H2-Production from ...", "x_template_1.zip");

	private final String label;
	private final String file;

	Template(String label, String file) {
		this.label = label;
		this.file = file;
	}

	String file() {
		return this.file;
	}

	String label() {
		return this.label;
	}
}
