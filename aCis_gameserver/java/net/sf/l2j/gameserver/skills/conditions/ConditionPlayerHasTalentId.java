package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.skills.Env;

public class ConditionPlayerHasTalentId extends Condition {

	private int talentId;
	
	public ConditionPlayerHasTalentId(int talentId) {
		this.talentId = talentId;
	}

	@Override
	public boolean testImpl(Env env) {
		if(!env.getCharacter().isPlayer()) {
			return false;
		}
		
		return env.getCharacter().getPlayer().hasTalent(talentId);
	}
}
