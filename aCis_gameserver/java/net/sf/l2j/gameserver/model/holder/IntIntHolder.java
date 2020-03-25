package net.sf.l2j.gameserver.model.holder;

import lombok.Data;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * A generic int/int container.
 */
@Data
public class IntIntHolder {

	private int id;
	private int value;

	public IntIntHolder(int id, int value) {
		this.id = id;
		this.value = value;
	}

	public final L2Skill getSkill() {
		return SkillTable.getInstance().getInfo(id, value);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": Id: " + id + ", Value: " + value;
	}
}
