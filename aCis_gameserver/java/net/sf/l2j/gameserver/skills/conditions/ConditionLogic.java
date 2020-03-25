/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.skills.conditions;

import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author finfan
 */
public abstract class ConditionLogic extends Condition {

	private Condition[] conditions = EMPTY_CONDITIONS;

	public final Condition[] getConditions() {
		return conditions;
	}

	public void add(Condition condition) {
		if (condition == null) {
			throw new IllegalStateException("Tried to add a 'null' condition to an <"
					+ getClass().getSimpleName().replace("ConditionLogic", "").toLowerCase() + "> condition");
		}

		conditions = (Condition[]) ArrayUtils.add(conditions, condition);
	}

	public Condition getCanonicalCondition() {
		if (conditions.length == 0) {
			throw new IllegalStateException("Empty <"
					+ getClass().getSimpleName().replace("ConditionLogic", "").toLowerCase() + "> condition");
		}

		if (conditions.length == 1) {
			return conditions[0];
		}

		return this;
	}

	@Override
	public int getMessageId() {
		for (Condition c : getConditions()) {
			int messageId = c.getMessageId();
			if (messageId != 0) {
				return messageId;
			}
		}

		return super.getMessageId();
	}

	@Override
	public final String getMessage() {
		for (Condition c : getConditions()) {
			String message = c.getMessage();
			if (message != null) {
				return message;
			}
		}

		return super.getMessage();
	}

	@Override
	public boolean isLogic() {
		return true;
	}
}
