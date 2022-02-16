package org.openlca.app.collaboration.viewers.json.content;

import java.util.Set;

import com.google.gson.JsonElement;

public interface IDependencyResolver {

	public Set<String> resolve(JsonElement parent, String property);

}