package org.openlca.app.sh2e;

import com.google.gson.JsonObject;
import org.openlca.core.model.ModelType;
import org.openlca.jsonld.Json;
import org.openlca.jsonld.JsonStoreReader;
import org.openlca.jsonld.ZipStore;
import org.openlca.util.Strings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class MappingZipStore implements JsonStoreReader, AutoCloseable {

	private final ZipStore zip;
	private final String category;
	private final Map<String, String> oldToNew = new HashMap<>();
	private final Map<String, String> newToOld = new HashMap<>();
	private String mappedSystemId;

	private MappingZipStore(ZipStore zip, String category) {
		this.zip = zip;
		this.category = category;
	}

	static MappingZipStore of(File file, String category) {
		try {
			var zip = ZipStore.open(file);
			var store = new MappingZipStore(zip, category);
			// fill the mappings
			store.getRefIds(ModelType.PRODUCT_SYSTEM);
			store.getRefIds(ModelType.PROCESS);
			return store;
		} catch (IOException e) {
			throw new RuntimeException("failed to open zip: " + file, e);
		}
	}

	String mappedSystemId() {
		return mappedSystemId;
	}

	private boolean skip(ModelType type) {
		return type != ModelType.PROCESS
				&& type != ModelType.PRODUCT_SYSTEM;
	}

	@Override
	public List<String> getRefIds(ModelType type) {
		var origin = zip.getRefIds(type);
		if (skip(type) || origin.isEmpty())
			return origin;
		var list = origin.stream()
				.map(this::mapIfAbsent)
				.toList();
		if (!list.isEmpty() && type == ModelType.PRODUCT_SYSTEM) {
			mappedSystemId = list.get(0);
		}
		return list;
	}

	private String mapIfAbsent(String oldId) {
		return oldToNew.computeIfAbsent(oldId, $ -> {
			var newId = UUID.randomUUID().toString();
			newToOld.put(newId, oldId);
			return newId;
		});
	}

	private String originalIdOf(String refId) {
		var oldId = newToOld.get(refId);
		return oldId != null ? oldId : refId;
	}

	@Override
	public List<String> getFiles(String dir) {
		return zip.getFiles(dir);
	}

	@Override
	public List<String> getBinFiles(ModelType type, String refId) {
		return skip(type)
				? zip.getBinFiles(type, refId)
				: zip.getBinFiles(type, originalIdOf(refId));
	}

	@Override
	public JsonObject get(ModelType type, String refId) {
		if (skip(type))
			return zip.get(type, refId);
		var obj = zip.get(type, originalIdOf(refId));
		if (obj == null)
			return null;

		swapIdOf(obj);

		// set a possible top-category
		if (Strings.notEmpty(category)) {
			var c = Json.getString(obj, "category");
			var path = Strings.notEmpty(c)
					? category + "/" + c
					: category;
			Json.put(obj, "category", path);
		}

		if (type == ModelType.PRODUCT_SYSTEM) {
			mapSystemRefs(obj);
		} else {
			Json.forEachObject(obj, "exchanges",
					e -> swapIdOf(Json.getObject(e, "defaultProvider")));
		}
		return obj;
	}

	private void mapSystemRefs(JsonObject obj) {
		swapIdOf(Json.getObject(obj, "refProcess"));
		Json.forEachObject(obj, "processes", this::swapIdOf);
		Json.forEachObject(obj, "processLinks", link -> {
			swapIdOf(Json.getObject(link, "process"));
			swapIdOf(Json.getObject(link, "provider"));
		});
		Json.forEachObject(obj, "parameterSets",
				set -> Json.forEachObject(set, "parameters",
						param -> swapIdOf(Json.getObject(param, "context"))));
	}

	private void swapIdOf(JsonObject obj) {
		if (obj == null)
			return;
		var oldId = Json.getString(obj, "@id");
		if (oldId == null)
			return;
		var newId = oldToNew.get(oldId);
		if (newId != null) {
			Json.put(obj, "@id", newId);
		}
	}

	@Override
	public byte[] getBytes(String path) {
		return zip.getBytes(path);
	}

	@Override
	public void close() throws IOException {
		zip.close();
	}
}
