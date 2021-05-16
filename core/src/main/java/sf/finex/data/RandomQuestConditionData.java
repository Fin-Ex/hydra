/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data;

import lombok.Data;
import sf.finex.enums.ERandomQuestType;

/**
 *
 * @author FinFan
 */
@Data
public class RandomQuestConditionData<T> {

	private final ERandomQuestType type;
	private final T target;
	private int value;

	public RandomQuestConditionData(ERandomQuestType type, T target, int value) {
		this.type = type;
		this.target = target;
		this.value = value;
	}

	public RandomQuestConditionData(ERandomQuestType type, T target) {
		this.type = type;
		this.target = target;
		this.value = 0;
	}

	public <T> T getTarget() {
		return (T) target;
	}
}
