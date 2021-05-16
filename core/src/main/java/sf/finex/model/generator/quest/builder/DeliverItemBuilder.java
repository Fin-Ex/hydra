/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.generator.quest.builder;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import sf.finex.data.QuestRewardData;
import sf.finex.data.RandomQuestConditionData;
import sf.finex.model.generator.quest.RandomQuestManager;
import sf.l2j.commons.math.MathUtil;
import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.data.ItemTable;
import sf.l2j.gameserver.data.NpcTable;
import sf.l2j.gameserver.model.World;
import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.template.NpcTemplate;
import sf.l2j.gameserver.model.holder.IntIntHolder;
import sf.l2j.gameserver.model.item.kind.Item;

/**
 * Deliver itme from sender NPC to receiver NPC. Reward is <b>adena</b> which
 * depends from distance of delivering.
 *
 * @author FinFan
 */
public class DeliverItemBuilder extends RandomQuestBuilder {

	public static final int DELIVER_ITEM_ID = 9214;

	@Override
	public void buildCondition() {
		// create sender hodler data with ID NPC and ID ITEM
		final List<NpcTemplate> list = RandomQuestManager.getInstance().getNpcs();
		final NpcTemplate senderNpc = Rnd.get(list);

		// create receiver hodler data with ID NPC and ID ITEM
		NpcTemplate receiverNpc = Rnd.get(list);
		while (receiverNpc == senderNpc) {
			receiverNpc = Rnd.get(list);
		}

		final DeliverData data = new DeliverData(senderNpc.getNpcId(), receiverNpc.getNpcId(), quest.getQuestItems().get(0));
		quest.setCondition(new RandomQuestConditionData(quest.getType(), data));
	}

	@Override
	public void buildRewards() {
		final DeliverData data = (DeliverData) quest.getCondition().getTarget();
		final NpcTemplate senderTemp = NpcTable.getInstance().getTemplate(data.senderId);
		final NpcTemplate receiverTemp = NpcTable.getInstance().getTemplate(data.receiverId);
		final Npc sender = World.getInstance().getNpcTemplates().get(senderTemp);
		final Npc receiver = World.getInstance().getNpcTemplates().get(receiverTemp);
		final int adena = (int) MathUtil.calculateDistance(sender, receiver, true);
		final QuestRewardData reward = new QuestRewardData(Item.ADENA, adena, false);
		quest.setRewards(new ArrayList<>());
		quest.getRewards().add(reward);
	}

	@Override
	public void buildDescription() {
		final DeliverData deliverHodler = (DeliverData) quest.getCondition().getTarget();
		final String receiver = deliverHodler.getReceiverName();

		StringBuilder descr = new StringBuilder();
		descr.append("<center>Description</center><br>");
		descr.append("\"Requires a courier who delivers the <font color=LEVEL>x")
				.append(deliverHodler.getItemCount()).append(" ")
				.append(deliverHodler.getItemName()).append("</font> to the <font color=LEVEL>").append(receiver).append("</font>. Payment upon delivery!")
				.append(" Get resources from the advertiser, and deliver them to <font color=LEVEL>").append(receiver).append("</font>.\"<br>")
				.append("Advertiser: <font color=LEVEL>").append(deliverHodler.getSenderName()).append("</font>");
		descr.append("<table width=272><tr>");
		descr.append("<td align=center><button value=\"Take\" action=\"bypass -h npc_%objectId%_takeQuest ").append(quest.getId()).append("\" width=74 height=24 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("<td align=center><button value=\"Cancel\" action=\"bypass -h npc_%objectId%_cancelQuest\" width=74 height=23 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("<td align=center><button value=\"Return\" action=\"bypass -h npc_%objectId%_showQuestList\" width=74 height=23 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("</tr></table>");

		quest.setDescription(descr.toString());
	}

	@Override
	public void buildName() {
		final DeliverData deliverHodler = (DeliverData) quest.getCondition().getTarget();
		quest.setName(quest.getType().getQuestName() + "x" + deliverHodler.getItemCount() + " " + deliverHodler.getItemName());
	}

	@Override
	public void buildExpAndSp() {
	}

	@Override
	public void buildQuestItems() {
		quest.setQuestItems(new ArrayList<>(1));
		quest.getQuestItems().add(new IntIntHolder(DELIVER_ITEM_ID, Rnd.get(1, (int) quest.getGrade().getAverageLevel())));
	}

	@Data
	public static class DeliverData {

		private final int senderId, receiverId;
		private final IntIntHolder itemHolder;

		public String getItemName() {
			return ItemTable.getInstance().getTemplate(itemHolder.getId()).getName();
		}

		public int getItemCount() {
			return itemHolder.getValue();
		}

		public String getSenderName() {
			return NpcTable.getInstance().getTemplate(senderId).getName();
		}

		public String getReceiverName() {
			return NpcTable.getInstance().getTemplate(receiverId).getName();
		}
	}
}
