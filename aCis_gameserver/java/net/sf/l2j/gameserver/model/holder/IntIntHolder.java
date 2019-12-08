package net.sf.l2j.gameserver.model.holder;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * A generic int/int container.
 */
public class IntIntHolder {

	private int id;
	private int value;

	public IntIntHolder(int id, int value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the L2Skill associated to the id/value.
	 */
	public final L2Skill getSkill() {
		return SkillTable.getInstance().getInfo(id, value);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": Id: " + id + ", Value: " + value;
	}
}
