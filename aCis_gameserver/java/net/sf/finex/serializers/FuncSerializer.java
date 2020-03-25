/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.sf.finex.Classes;
import net.sf.l2j.gameserver.skills.basefuncs.Func;

/**
 *
 * @author finfan
 */
public class FuncSerializer implements JsonSerializer<Func>, JsonDeserializer<Class<?>> {

	@Override
	public JsonElement serialize(Func t, Type type, JsonSerializationContext jsc) {
		final JsonObject object = new JsonObject();
		object.addProperty("operation", t.getClass().getSimpleName().substring(4));
		return object;
	}

	@Override
	public Class<?> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
		return Classes.getClass("net.sf.l2j.gameserver.skills.basefuncs.Func" + je.getAsString());
	}
	
}
