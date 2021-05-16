/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.generator.quest.builder;

import java.util.ArrayList;
import java.util.List;
import sf.finex.data.QuestRewardData;
import sf.finex.data.RandomQuestConditionData;
import sf.l2j.commons.random.Rnd;
import sf.l2j.gameserver.data.ItemTable;
import sf.l2j.gameserver.model.item.kind.Item;

/**
 * Quest for finding resources (charcoal, steel, etc...) Description: Find
 * needable resource count and return to the quest board. You will receive
 * reward model [<b>Item</b>]. Item is enchant scroll for weapon or armor. Armor
 * weapon scroll count depends from random 1 or 3 max. Weapon scrolls every time
 * is only 1.
 *
 * @author FinFan
 */
public class FindResourcesBuilder extends RandomQuestBuilder {

	public static final Integer[] RESOURCE_IDS = {
		1872,
		1867,
		1878,
		1870,
		1869,
		1880,
		1883,
		1864,
		1866,
		1868,
		1865,
		1871,
		1881,
		1879,
		1884,
		1885,
		1882,
		1886,
		1873,
		1877,
		1891,
		1892,
		1889,
		1894,
		5220,
		1895,
		1890,
		1876,
		1893,
		1874,
		1875,
		1888,
		1887,
		4043,
		4047,
		4042,
		4046,
		4045,
		4048,
		4039,
		4040,
		4041,
		4044,
		5553,
		5550,
		5551,
		5549,
		5554,
		5552
	};

	@Override
	public void buildCondition() {
		// create the resource from level
		final Item resource = ItemTable.getInstance().getTemplate(Rnd.get(RESOURCE_IDS));
		final int count = (int) Math.max(((quest.getGrade().ordinal() + 1 * 300) * (300. / resource.getReferencePrice())), 1);
		quest.setCondition(new RandomQuestConditionData(quest.getType(), resource, count));
	}

	@Override
	public void buildRewards() {
		final List<QuestRewardData> list = new ArrayList<>();
		final int chance = (int) Math.sqrt(quest.getCondition().getValue());
		final boolean success = Rnd.get(chance) > 100;
		final int cnt = Rnd.calcGuarantee(100, new Integer[]{3, 2, 1});

		switch (quest.getGrade()) {
			case NG:
				//reward enchant scroll D grade for armor
				list.add(new QuestRewardData(956, 1, true));
				break;

			case D:
				//reward enchant scroll D grade for armor & weapon with chances
				if (success) {
					list.add(new QuestRewardData(956, 1, true));
				} else {
					list.add(new QuestRewardData(955, cnt, true));
				}
				break;

			case C:
				if (success) {
					list.add(new QuestRewardData(951, 1, true));
				} else {
					list.add(new QuestRewardData(952, cnt, true));
				}
				break;

			case B:
				if (success) {
					list.add(new QuestRewardData(947, 1, true));
				} else {
					list.add(new QuestRewardData(948, cnt, true));
				}
				break;

			case A:
				if (success) {
					list.add(new QuestRewardData(729, 1, true));
				} else {
					list.add(new QuestRewardData(730, cnt, true));
				}
				break;

			case S:
				if (success) {
					list.add(new QuestRewardData(959, 1, true));
				} else {
					list.add(new QuestRewardData(960, cnt, true));
				}
				break;
		}
		quest.setRewards(list);
	}

	@Override
	public void buildDescription() {
		final StringBuilder descr = new StringBuilder();
		final RandomQuestConditionData cond = quest.getCondition();
		final Item item = (Item) cond.getTarget();
		descr.append("<center>Description</center><br>");
		descr.append("Bring the <font color=LEVEL>").append(item.getName()).append(" x").append(cond.getValue())
				.append("</font> and you will be richly rewarded!<br1>When you finish, do not forget to put a mark on the task (at the board) that it is done.");
		descr.append("<table width=272>");
		descr.append("<table><tr>");
		descr.append("<td align=center><button value=\"Take\" action=\"bypass -h npc_%objectId%_takeQuest ").append(quest.getId()).append("\" width=74 height=24 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("<td align=center><button value=\"Cancel\" action=\"bypass -h npc_%objectId%_cancelQuest\" width=74 height=23 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("<td align=center><button value=\"Return\" action=\"bypass -h npc_%objectId%_showQuestList\" width=74 height=23 fore=\"L2UI_CH3.btn1_normal\" back=\"L2UI_CH3.btn1_normal_over\"></td>");
		descr.append("</tr></table>");
		quest.setDescription(descr.toString());
	}

	@Override
	public void buildName() {
		quest.setName(quest.getType().getQuestName() + ((Item) quest.getCondition().getTarget()).getName());
	}

	@Override
	public void buildExpAndSp() {
	}

	@Override
	public void buildQuestItems() {
	}
}
