package org.openlca.app.sh2e;

import org.openlca.app.util.ErrorReporter;
import org.openlca.core.DataDir;
import org.openlca.core.database.IDatabase;
import org.openlca.core.model.ProductSystem;
import org.openlca.jsonld.input.JsonImport;
import org.openlca.jsonld.input.UpdateMode;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

class TemplateImport {

	private final Template template;
	private final String category;
	private final IDatabase db;

	TemplateImport(
		Template template, String category, IDatabase db
	) {
		this.template = template;
		this.category = category;
		this.db = db;
	}

	Optional<ProductSystem> run() {
		var tempZip = asTempZip(template);
		if (tempZip == null)
			return Optional.empty();

		String systemId;
		try (var store = MappingZipStore.of(tempZip, category)) {
			new JsonImport(store, db)
				.setUpdateMode(UpdateMode.NEVER)
				.run();
			systemId = store.mappedSystemId();
		} catch (Exception e) {
			ErrorReporter.on("Failed to import template", e);
			return Optional.empty();
		}

		try {
			Files.delete(tempZip.toPath());
		} catch (IOException e) {
			LoggerFactory.getLogger(getClass())
				.warn("failed to delete temp.-file", e);
		}

		var system = db.get(ProductSystem.class, systemId);
		return Optional.ofNullable(system);
	}

	private File asTempZip(Template t) {
		var stream = getClass().getResourceAsStream(t.file());
		if (stream == null)
			return null;
		try (stream) {
			var temp = Files.createTempFile("sh2e", ".zip");
			Files.copy(stream, temp, StandardCopyOption.REPLACE_EXISTING);
			return temp.toFile();
		} catch (Exception e) {
			ErrorReporter.on("failed to extract template", e);
			return null;
		}
	}
}

