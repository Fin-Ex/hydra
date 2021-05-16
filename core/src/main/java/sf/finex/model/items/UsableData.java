/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.items;

import java.util.List;
import lombok.Data;
import sf.l2j.gameserver.model.holder.IntIntHolder;
import sf.l2j.gameserver.model.item.type.ActionType;
import sf.l2j.gameserver.model.item.type.EtcItemType;

/**
 *
 * @author finfan
 */
@Data
public class UsableData extends ItemData {

	private EtcItemType type;
	private ActionType action;
	private String handler;
	private List<IntIntHolder> skills;

	@Override
	public boolean isUsable() {
		return true;
	}
}