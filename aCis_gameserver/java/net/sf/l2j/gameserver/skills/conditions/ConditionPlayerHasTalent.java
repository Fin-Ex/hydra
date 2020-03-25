package net.sf.l2j.gameserver.skills.conditions;


import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.skills.Env;

public class ConditionPlayerHasTalent extends Condition {

	private SkillTable.FrequentTalent talent;

	public ConditionPlayerHasTalent(SkillTable.FrequentTalent talent) {
		this.talent = talent;
	}

	@Override
	public boolean testImpl(Env env) {
		if(!env.getCharacter().isPlayer()) {
			return false;
		}
		
		return env.getCharacter().getPlayer().hasTalent(talent);
	}
}
