/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sf.finex.data.tables.TalentTable;
import sf.l2j.gameserver.model.base.ClassId;

/**
 *
 * @author FinFan
 */
@Slf4j
@Data
public class TalentBranchData {

	private final int id;
	private final ClassId classId;
	private final int[] talents;

	public final TalentData getTalent(int id) {
		for (int talentId : talents) {
			if (talentId == id) {
				return TalentTable.getInstance().get(id);
			}
		}

		return null;
	}

	public final boolean contains(int talentId) {
		for (int i = 0; i < talents.length; i++) {
			if (talents[i] == talentId) {
				return true;
			}
		}

		return false;
	}
}
