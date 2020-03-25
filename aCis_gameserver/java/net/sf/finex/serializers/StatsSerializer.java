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
import net.sf.l2j.gameserver.skills.Stats;

/**
 *
 * @author finfan
 */
public class StatsSerializer implements JsonSerializer<Stats>, JsonDeserializer<Stats> {

	@Override
	public JsonElement serialize(Stats t, Type type, JsonSerializationContext jsc) {
		return new JsonPrimitive(t.name());
	}

	@Override
	public Stats deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
		return Stats.valueOfXml(je.getAsString());
	}
	
}
