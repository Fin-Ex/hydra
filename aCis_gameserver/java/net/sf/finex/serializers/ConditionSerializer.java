/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;
import net.sf.finex.Classes;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.conditions.ConditionLogic;
import net.sf.l2j.gameserver.skills.conditions.ConditionLogicAnd;
import net.sf.l2j.gameserver.skills.conditions.ConditionLogicNot;
import net.sf.l2j.gameserver.skills.conditions.ConditionLogicOr;
import net.sf.l2j.gameserver.skills.conditions.EmptyCondition;

/**
 *
 * @author finfan
 */
public class ConditionSerializer implements JsonSerializer<Condition>, JsonDeserializer<Condition> {

	@Override
	public Condition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return deserializeOperator(OperateType.and, json, context);
	}

	private Condition deserializeOperator(OperateType type, JsonElement element, JsonDeserializationContext context) {
		if(element.isJsonObject()) {
			return deserializeCondition(context, element.getAsJsonObject(), null);
		}
		else {
			JsonArray array = element.getAsJsonArray();
			if(array.size() > 1) {
				ConditionLogic condition = getConditionFromOperateType(type);
				for(int i = 0; i < array.size(); i++) {
					deserializeCondition(context, array.get(i), condition);
				}
				return condition;
			}
			else {
				if(array.size() == 0) {
					return EmptyCondition.getInstance();
				}
				return deserializeCondition(context, array.get(0), null);
			}
		}
	}

	private Condition deserializeCondition(JsonDeserializationContext context, JsonElement nextElement, ConditionLogic condition) {
		JsonObject asJsonObject = nextElement.getAsJsonObject();
		if(asJsonObject.has("type")) {
			final String name = asJsonObject.get("type").getAsString();
			Class<? extends Condition> cond = (Class<? extends Condition>) Classes.getClass(name);
			Condition deserialize = context.deserialize(nextElement, cond);
			if(condition == null) {
				return deserialize;
			}
			condition.add(deserialize);
			return condition;
		}
		else {
			for(Map.Entry<String, JsonElement> entry : asJsonObject.entrySet()) {
				String key = entry.getKey();
				JsonElement value = entry.getValue();
				if(value.isJsonArray()) {
					switch(key) {
						case "and":
						case "or":
						case "not": {
							Condition cond = deserializeOperator(OperateType.valueOf(key), entry.getValue(), context);
							if(condition != null) {
								condition.add(cond);
								return condition;
							}
							return cond;
						}
						default: {
							throw new UnsupportedOperationException(key);
						}
					}
				}
			}
		}
		return condition;
	}

	private static ConditionLogic getConditionFromOperateType(OperateType operateType) {
		switch(operateType) {
			case or:
				return new ConditionLogicOr();
			case and:
				return new ConditionLogicAnd();
			case not:
				return new ConditionLogicNot();
		}
		throw new NullPointerException(operateType.name());
	}

	private static OperateType getOperateTypeFromCondition(ConditionLogic conditionLogic) {
		if(conditionLogic instanceof ConditionLogicAnd) {
			return OperateType.and;
		}
		else if(conditionLogic instanceof ConditionLogicOr) {
			return OperateType.or;
		}
		else if(conditionLogic instanceof ConditionLogicNot) {
			return OperateType.not;
		}
		throw new NullPointerException();
	}

	@Override
	public JsonElement serialize(Condition src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonArray jsonArray = new JsonArray();
		serializeWhileOperate(src, jsonArray, context);
		return jsonArray;
	}

	private void serializeWhileOperate(Condition condition, JsonArray jsonArray, JsonSerializationContext context) {
		if(condition.isLogic()) {
			ConditionLogic logic = (ConditionLogic) condition;
			String name = getOperateTypeFromCondition(logic).name();
			JsonObject object = new JsonObject();
			JsonArray array = new JsonArray();
			object.add(name, array);
			for(Condition logicCondition : logic.getConditions()) {
				if(logicCondition.isLogic()) {
					serializeWhileOperate(logicCondition, array, context);
				}
				else {
					serializeScriptData(logicCondition, array, context);
				}
			}
			jsonArray.add(object);

		}
		else {
			//catch the first wrong cond(not logic condition), every first incoming cond should be logical(ConditionLogic)
			serializeScriptData(condition, jsonArray, context);
		}
	}

	private void serializeScriptData(Condition condition, JsonArray jsonArray, JsonSerializationContext context) {
		JsonElement serialize = context.serialize(condition);
		jsonArray.add(serialize);
	}

	private enum OperateType {
		and,
		or,
		not
	}
	
}
