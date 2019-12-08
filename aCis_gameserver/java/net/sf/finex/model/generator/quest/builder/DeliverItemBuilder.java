/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import net.sf.finex.data.QuestRewardData;
import net.sf.finex.data.RandomQuestConditionData;
import net.sf.finex.enums.EGradeType;
import net.sf.finex.model.generator.quest.RandomQuestManager;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.Jewel;
import net.sf.l2j.gameserver.model.item.kind.Weapon;

/**
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
		while(receiverNpc == senderNpc) {
			receiverNpc = Rnd.get(list);
		}
		
		final DeliverData data = new DeliverData(senderNpc.getNpcId(), receiverNpc.getNpcId(), quest.getQuestItems().get(0));
		quest.setCondition(new RandomQuestConditionData(quest.getType(), data, 0));
	}

	@Override
	public void buildRewards() {
		QuestRewardData reward;
		byte rewardType = 0;// adena
		final DeliverData deliverData = (DeliverData) quest.getCondition().getTarget();
		final int deliverRate = deliverData.getItemHolder().getValue();
		if (Rnd.get(deliverRate) > 100) {
			rewardType = 1; // jewel
		} else if (Rnd.get(deliverRate) > 100) {
			rewardType = 2; // armor
		} else if (Rnd.get(deliverRate) > 100) {
			rewardType = 3; // weapon
		}

		final EGradeType grade = quest.getGrade();
		switch (rewardType) {
			// jewel reward
			case 1: {
				final Map<EGradeType, List<Jewel>> rewardedItems = new HashMap<>();
				for (Jewel jwl : ItemTable.JEWELS.values()) {
					if (jwl.getCrystalType() == grade) {
						if (!rewardedItems.containsKey(grade)) {
							rewardedItems.put(grade, new ArrayList<>());
						}
						rewardedItems.get(grade).add(jwl);
					}
				}
				final Jewel rewardArmor = Rnd.get(rewardedItems.get(grade));
				reward = new QuestRewardData(rewardArmor.getItemId(), 1, false);
			}
			break;

			// armor reward
			case 2: {
				final Map<EGradeType, List<Armor>> rewardedItems = new HashMap<>();
				for (Armor armor : ItemTable.ARMORS.values()) {
					if (armor.getCrystalType() == grade) {
						if (!rewardedItems.containsKey(grade)) {
							rewardedItems.put(grade, new ArrayList<>());
						}
						rewardedItems.get(grade).add(armor);
					}
				}

				// sort by price
				final Armor rewardArmor = Rnd.get(rewardedItems.get(grade));
				reward = new QuestRewardData(rewardArmor.getItemId(), 1, false);
			}
			break;

			// weapon reward
			case 3: {
				final Map<EGradeType, List<Weapon>> rewardedItems = new HashMap<>();
				for (Weapon wpn : ItemTable.WEAPONS.values()) {
					if (wpn.getCrystalType() == grade) {
						if (!rewardedItems.containsKey(grade)) {
							rewardedItems.put(grade, new ArrayList<>());
						}
						rewardedItems.get(grade).add(wpn);
					}
				}
				final Weapon rewardArmor = Rnd.get(rewardedItems.get(grade));
				reward = new QuestRewardData(rewardArmor.getItemId(), 1, false);
			}
			break;

			// adena reward
			default: {
				double firstRandom = Math.pow(grade.getAverageLevel() * 10, 2);
				firstRandom *= deliverData.getItemCount() / 100. + 1;
				reward = new QuestRewardData(57, (int) firstRandom, false);
			}
			break;
		}

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
		final double staticValue = Math.pow(quest.getGrade().getAverageLevel() * 1000., quest.getGrade().getAverageLevel() / 1000. + 1);
		quest.setExp((long) staticValue);
		quest.setSp((int) (Math.sqrt(staticValue) * 25));
	}

	@Override
	public void buildQuestItems() {
		quest.setQuestItems(new ArrayList<>(1));
		quest.getQuestItems().add(new IntIntHolder(DELIVER_ITEM_ID, Rnd.get(1, 50)));
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
