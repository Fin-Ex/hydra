/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.instance;

import sf.finex.model.GLT.GLTController;
import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.network.serverpackets.ActionFailed;
import sf.l2j.gameserver.network.serverpackets.MoveToPawn;

/**
 *
 * @author finfan
 */
public class GLTTrader extends GLTNpc {

	public GLTTrader(int objectId, NpcTemplate template) {
		super(objectId, template);
	}

	@Override
	public void showChatWindow(Player player) {
		if (!GLTController.getInstance().isParticipate(player)) {
			showChatWindow(player, "data/html/glt_trader-no.htm");
			return;
		}

		super.showChatWindow(player);
	}

	@Override
	public void showChatWindow(Player player, String filename) {
		if (!GLTController.getInstance().isParticipate(player)) {
			showChatWindow(player, "data/html/glt_trader-no.htm");
			return;
		}

		super.showChatWindow(player, filename);
	}

	@Override
	public void onAction(Player player) {
		// Set the target of the player
		if (player.getTarget() != this) {
			player.setTarget(this);
		} else {
			if (!canInteract(player)) {
				player.getAI().setIntention(CtrlIntention.INTERACT, this);
			} else {
				// Rotate the player to face the instance
				player.sendPacket(new MoveToPawn(player, this, INTERACTION_DISTANCE));
				player.sendPacket(ActionFailed.STATIC_PACKET);
				if (hasRandomAnimation()) {
					onRandomAnimation(Rnd.get(8));
				}
			}
		}
	}
}
