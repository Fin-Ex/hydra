/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author finfan
 */
public class OrderSerializer implements JsonSerializer<Integer>, JsonDeserializer<Integer> {

	@Override
	public JsonElement serialize(Integer t, Type type, JsonSerializationContext jsc) {
		return new JsonPrimitive("0x0" + Integer.toHexString(t));
	}

	@Override
	public Integer deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
		return Integer.decode(je.getAsJsonObject().get("order").getAsString());
	}
}
