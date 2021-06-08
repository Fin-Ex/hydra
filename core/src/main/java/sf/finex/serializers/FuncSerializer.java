/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import sf.finex.utils.Classes;
import sf.l2j.gameserver.skills.basefuncs.Func;

import java.lang.reflect.Type;

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
		return Classes.getClass("sf.l2j.gameserver.skills.basefuncs.Func" + je.getAsString());
	}
	
}
