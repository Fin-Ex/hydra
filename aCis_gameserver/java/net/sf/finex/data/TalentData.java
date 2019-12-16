/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data;

import lombok.Data;
import net.sf.l2j.gameserver.data.SkillTable;

/**
 *
 * @author FinFan
 */
@Data
public class TalentData {

	private final int id;
	private final int requiredLevel;
	private final int requiredTalent;
	private final int requiredSkill;
	private final int skillId;
	private final String name, descr;

	public final String getIcon() {
		return "v1c01.talent_" + SkillTable.getInstance().getInfo(skillId, 1).getName().replace(" ", "_");
	}
}
