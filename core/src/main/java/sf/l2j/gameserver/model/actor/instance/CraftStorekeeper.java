/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.cache.HtmCache;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 *
 * @author FinFan
 */
public class CraftStorekeeper extends Folk {

	public CraftStorekeeper(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command) {
		super.onBypassFeedback(player, command);
	}

	@Override
	public String getHtmlPath(int npcId, int val) {
		String filename;

		if (val == 0) {
			filename = "data/html/craft/storekeeper/" + npcId + ".htm";
		} else {
			filename = "data/html/craft/storekeeper/" + npcId + "-" + val + ".htm";
		}

		if (HtmCache.getInstance().isLoadable(filename)) {
			return filename;
		}

		return "data/html/npcdefault.htm";
	}
}
