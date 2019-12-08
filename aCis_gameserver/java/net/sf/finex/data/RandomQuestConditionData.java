/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data;

import lombok.Data;
import net.sf.finex.enums.ERandomQuestType;

/**
 *
 * @author FinFan
 */
@Data
public class RandomQuestConditionData<T> {
	private final ERandomQuestType type;
	private final T target;
	private final int count;
	
	public <T> T getTarget() {
		return (T) target;
	}
}
