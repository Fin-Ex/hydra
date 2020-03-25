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
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.sf.l2j.gameserver.skills.basefuncs.Lambda;
import net.sf.l2j.gameserver.skills.basefuncs.LambdaConst;

/**
 *
 * @author finfan
 */
public class LambdaSerializer implements JsonSerializer<Lambda>, JsonDeserializer<Lambda> {

	@Override
	public JsonElement serialize(Lambda t, Type type, JsonSerializationContext jsc) {
		return new JsonPrimitive(((LambdaConst) t).getValue());
	}

	@Override
	public Lambda deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
		final JsonObject object = je.getAsJsonObject();
		return new LambdaConst(Double.parseDouble(object.get("lambda").getAsString()));
	}
	
}
