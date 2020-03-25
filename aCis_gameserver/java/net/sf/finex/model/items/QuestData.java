/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.items;

import java.util.List;
import lombok.Data;
import net.sf.l2j.gameserver.model.item.type.EtcItemType;
import net.sf.l2j.gameserver.scripting.Quest;

/**
 *
 * @author finfan
 */
@Data
public class QuestData extends ItemData {

	private EtcItemType type;
	private transient List<Quest> questEvents;

	@Override
	public boolean isQuest() {
		return true;
	}
}
