/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest.handler;

import net.sf.finex.data.RandomQuestData;
import net.sf.finex.model.generator.quest.RandomQuestComponent;
import static net.sf.finex.model.generator.quest.builder.DeliverItemBuilder.DELIVER_ITEM_ID;
import net.sf.finex.model.generator.quest.builder.DeliverItemBuilder.DeliverData;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnTalk;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 *
 * @author FinFan
 */
public class DeliverItemHandler extends RandomQuestHandler {

	@Override
	protected boolean onPassQuest(Player player, Npc npc) {
		final RandomQuestComponent component = player.getComponent(RandomQuestComponent.class);
		final RandomQuestData quest = component.getQuest();
		if (quest == null) {
			return false;
		}

		final DeliverData data = (DeliverData) quest.getCondition().getTarget();
		final int receiverId = data.getReceiverId();
		if (npc.getNpcId() != receiverId) {
			return false;
		}

		final ItemInstance deliverGoods = player.getInventory().getItemByItemId(data.getItemHolder().getId());
		return player.destroyItem("DeliverSuccess", deliverGoods, null, true);
	}

	@Override
	protected void onTalk(OnTalk event) {
		final RandomQuestComponent component = event.getTalker().getComponent(RandomQuestComponent.class);
		final Player player = event.getTalker();
		final Npc npc = event.getTarget();
		if (component.hasQuest()) {
			final DeliverData deliverData = (DeliverData) component.getQuest().getCondition().getTarget();
			// get delivering items from NPC
			if (npc.getNpcId() == deliverData.getSenderId() && !player.getInventory().hasAtLeastOneItem(deliverData.getItemHolder().getId())) {
				final Item deliverItem = ItemTable.getInstance().getTemplate(deliverData.getItemHolder().getId());
				final int weight = deliverItem.getWeight() * deliverData.getItemHolder().getValue();
				if (player.getInventory().validateWeight(weight)) {
					player.addItem("DeliverQuestItem", ItemTable.getInstance().createItem("DeliveringItem", DELIVER_ITEM_ID, deliverData.getItemCount(), player, npc), npc, true);
					final StringBuilder sb = new StringBuilder();
					sb.append("<html><body><br>");
					sb.append("Are you a courier?<br1>");
					sb.append("Well, take these resources and take them to <font color=LEVEL>").append(deliverData.getReceiverName()).append("</font>.<br1>");
					sb.append("I do not care how you do it, the main thing to do. Payment will be received upon arrival. Maybe...");
					sb.append("</body></html>");
					final NpcHtmlMessage html = new NpcHtmlMessage(event.getTarget().getObjectId());
					html.setHtml(sb.toString());
					player.sendPacket(html);
				} else {
					player.sendPacket(SystemMessageId.WEIGHT_LIMIT_EXCEEDED);
				}
			} else {
				super.onTalk(event);
			}
		} else {
			super.onTalk(event);
		}
	}
}
