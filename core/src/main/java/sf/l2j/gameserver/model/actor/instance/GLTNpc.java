/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.instance;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;

/**
 *
 * @author finfan
 */
public class GLTNpc extends Folk {
	
	public GLTNpc(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void onAction(Player player) {
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}