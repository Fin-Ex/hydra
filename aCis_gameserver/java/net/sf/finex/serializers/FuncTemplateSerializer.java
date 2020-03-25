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
import net.sf.l2j.gameserver.skills.Stats;
import net.sf.l2j.gameserver.skills.basefuncs.FuncTemplate;
import net.sf.l2j.gameserver.skills.basefuncs.Lambda;
import net.sf.l2j.gameserver.skills.basefuncs.LambdaConst;

/**
 *
 * @author finfan
 */
public class FuncTemplateSerializer implements JsonSerializer<FuncTemplate>, JsonDeserializer<FuncTemplate> {

	@Override
	public JsonElement serialize(FuncTemplate t, Type type, JsonSerializationContext jsc) {
		final JsonObject object = new JsonObject();
		object.addProperty("order", "0x0" + Integer.toHexString(t.order));

		final StringBuilder sb = new StringBuilder();
		//sb.append(t.stat).append(" ").append(t.func.getSimpleName().substring(4).toLowerCase()).append(" ").append(((LambdaConst) t.lambda).getValue());
		object.addProperty("stat", sb.toString());

//		if (t.applayCond != null) {
//			JsonArray array = new JsonArray();
//			serializeWhileOperate(t.applayCond, array, jsc);
//			object.add("condition", array);
//		}

		return object;
	}

	@Override
	public FuncTemplate deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
		final JsonObject object = je.getAsJsonObject();

		// convert from json to object variables
		final int orderProp = Integer.decode(object.get("order").getAsString());
		final String[] statProp = object.get("stat").getAsString().split(" ");
		final Stats stat = Stats.valueOf(statProp[0]);
		String func = null;
		switch (statProp[1]) {
			case "add":
				func = "Add";
				break;
			case "sub":
				func = "Sub";
				break;
			case "div":
				func = "Div";
				break;
			case "mul":
				func = "Mul";
				break;
			case "basemul":
				func = "BaseMul";
				break;
			case "set":
				func = "Set";
				break;
			case "ench":
				func = "Enchant";
				break;
			case "subdiv":
				func = "SubDiv";
				break;
			case "addmul":
				func = "AddMul";
				break;
			default:
				throw new UnsupportedOperationException("func symbol dont used: " + statProp[1]);
		}

		final Lambda lambda = new LambdaConst(Double.valueOf(statProp[2]));
		
		return new FuncTemplate(null, null, func, stat, orderProp, lambda);
	}
}
