package org.openlca.app.sh2e;

import com.google.gson.JsonObject;
import org.openlca.app.sh2e.Sh2e.Option;
import org.openlca.app.sh2e.Sh2e.Scope;
import org.openlca.jsonld.Json;
import org.openlca.util.Strings;

import java.util.EnumMap;
import java.util.Map;

class OptionSettings {

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

	Option get(Scope scope) {
		return scope != null
				? map.getOrDefault(scope, Option.Empty)
				: Option.Empty;
	}

	static OptionSettings readFrom(JsonObject obj) {
		var settings = new OptionSettings();
		if (obj == null)
			return settings;
		for (var scope : Scope.values()) {
			var s = Json.getString(obj, scope.label());
			if (Strings.nullOrEmpty(s))
				continue;
			var option = Sh2e.optionOf(s,
					scope.options().toArray(new Option[0]));
			settings.put(scope, option);
		}
		return settings;
	}
}
