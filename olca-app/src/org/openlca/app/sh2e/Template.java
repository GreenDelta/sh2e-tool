package org.openlca.app.sh2e;

enum Template {

	FCEV("LCA of FCEV", "x_LCA_of_FCEV.zip");

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
