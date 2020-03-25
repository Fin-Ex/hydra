/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.talents;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.finex.dao.PlayerLineageDao;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author FinFan
 */
@Slf4j
public class LineagePointsManager {

	@Getter private static final LineagePointsManager instance = new LineagePointsManager();

	public void giveLineagePoints(Player player) {
		if (player.getLevel() < player.getLineageReachLevel()) {
			return;
		}

		int points = 0;
		for (int i = 0; i < Config.LINEAGE_REACH_LEVEL.length; i++) {
			if (player.getLevel() >= Config.LINEAGE_REACH_LEVEL[i] && player.getLineageReachLevel() < Config.LINEAGE_REACH_LEVEL[i]) {
				points++;
			}
		}
		player.setLineagePoints(player.getLineagePoints() + points);
		player.setLineageReachLevel(player.getLevel());
		PlayerLineageDao.update(player);
	}

	/**
	 * Delete all {@code isTalent()} skills from player.<br>
	 * Regive all lineage points from reach level.<br>
	 * Increase the LP modifier for next reset.<br>
	 * Update DB.
	 *
	 * @param player character which reset his talents (by classIndex)
	 * @param free if reset is free we not increase resetPrice
	 */
	public void resetTalents(Player player, boolean free) {
		player.getSkills().values().stream().filter(skill -> skill.isTalent()).forEach(skill -> player.removeSkill(skill));
		player.sendSkillList();
		int pointsToAdd = 0;
		for (int i = 0; i < Config.LINEAGE_REACH_LEVEL.length; i++) {
			if (player.getLevel() >= Config.LINEAGE_REACH_LEVEL[i]) {
				pointsToAdd++;
			}
		}
		player.setLineagePoints(pointsToAdd);
		if (!free) {
			final int resetPrice = (int) (player.getLineageResetPrice() * Config.TALENT_RESET_MODIFIER);
			player.setLineageResetPrice(resetPrice);
		}
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.MASTERY_TREE_WAS_RESETED_AND_S1_LP_WAS_RETURNED).addNumber(pointsToAdd));
		PlayerLineageDao.update(player);
	}

	/**
	 * Checks talent reset id and price.<br>
	 * If {@code Config.TALENT_RESET_ID} is 0, we must <b>SP</b> for
	 * payment.<br>
	 * If {@code Config.TALENT_RESET_PRICE} is 0 the talent reset function will
	 * be <b>free</b>.
	 *
	 * @param player to check
	 * @param withPayment if true, consumes resources for learning
	 * @return [-1] FREE<br>[0] false check<br>[1] success check
	 */
	public byte validateReset(Player player, boolean withPayment) {
		boolean result = true;
		if (Config.TALENT_RESET_ID > 0) {
			final ItemInstance item = player.getInventory().getItemByItemId(Config.TALENT_RESET_ID);
			if (item != null) {
				// calc items count for reseting
				final int count = (int) Math.max(player.getLineageResetPrice(), 1);
				result = !(count > 0 && item.getCount() < count);
				if (!result) {
					if (item.getItemId() == Item.ADENA) {
						player.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
					} else {
						player.sendPacket(SystemMessageId.NOT_ENOUGH_REQUIRED_ITEMS);
						result = false;
					}
				} else {
					if (withPayment) {
						if (!player.destroyItemByItemId("TalentReset", Config.TALENT_RESET_ID, count, null, true)) {
							player.sendPacket(SystemMessageId.NOT_ENOUGH_REQUIRED_ITEMS);
							result = false;
						}
					}
				}
			}
		} else {
			int reqSP = player.getLineageResetPrice();
			if (reqSP <= 0) {
				// free reset
				return -1;
			}

			if (player.getSp() < reqSP) {
				player.sendPacket(SystemMessageId.NOT_ENOUGH_SP);
				result = false;
			} else {
				if (withPayment) {
					player.getStat().removeExpAndSp(0, reqSP);
				}
			}
		}
		return (byte) (result ? 1 : 0);
	}

	/**
	 * Calls <b>ONLY AFTER ADD OR MODIFY</b> SubClass.<br>
	 * <ul>
	 * <li>Insert new lineage data to DB</li>
	 * <li>Reset all LP to 0</li>
	 * <li>Reset reached level</li>
	 * <li>Reset lineage modifier</li>
	 * <li>Update DB with a new values</li>
	 * </ul>
	 *
	 * @param player subclass changer
	 */
	public void prepareForNewSubclass(Player player) {
		PlayerLineageDao.insert(player);
		player.setLineagePoints(0);
		player.setLineageReachLevel(player.getLevel());
		player.setLineageResetPrice(Config.TALENT_RESET_PRICE);
		PlayerLineageDao.update(player);
	}
}
