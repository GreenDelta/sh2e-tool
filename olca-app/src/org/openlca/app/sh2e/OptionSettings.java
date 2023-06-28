package org.openlca.app.sh2e;

import com.google.gson.JsonObject;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.jsonld.Json;

import java.util.EnumMap;
import java.util.Map;

public class OptionSettings {

	private final Map<Scope, Option> map = new EnumMap<>(Scope.class);

	OptionSettings put(Scope scope, Option option) {
		if (scope != null) {
			map.put(scope, option);
		}
		return this;
	}

	JsonObject toJson() {
		var obj = new JsonObject();
		for (var e : map.entrySet()) {
			var scope = e.getKey();
			var option = e.getValue();
			if (scope == null || option == null)
				continue;
			Json.put(obj, scope.label(), option.label());
		}
		return obj;
	}
}
