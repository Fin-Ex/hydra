package net.sf.l2j.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.L2Fishing;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class FishingSkill implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.PUMPING,
		ESkillType.REELING
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (!(activeChar instanceof Player)) {
			return;
		}

		final Player player = (Player) activeChar;
		final boolean isReelingSkill = skill.getSkillType() == ESkillType.REELING;

		final L2Fishing fish = player.getFishCombat();
		if (fish == null) {
			player.sendPacket((isReelingSkill) ? SystemMessageId.CAN_USE_REELING_ONLY_WHILE_FISHING : SystemMessageId.CAN_USE_PUMPING_ONLY_WHILE_FISHING);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}

		final Weapon weaponItem = player.getActiveWeaponItem();
		final ItemInstance weaponInst = activeChar.getActiveWeaponInstance();

		if (weaponInst == null || weaponItem == null) {
			return;
		}

		final int ssBonus = (activeChar.isChargedShot(ShotType.FISH_SOULSHOT)) ? 2 : 1;
		final double gradeBonus = 1 + weaponItem.getCrystalType().getId() * 0.1;

		int damage = (int) (skill.getPower() * gradeBonus * ssBonus);
		int penalty = 0;

		// Fish expertise penalty if skill level is superior or equal to 3.
		if (skill.getLevel() - player.getSkillLevel(1315) >= 3) {
			penalty = 50;
			damage -= penalty;

			player.sendPacket(SystemMessageId.REELING_PUMPING_3_LEVELS_HIGHER_THAN_FISHING_PENALTY);
		}

		if (ssBonus > 1) {
			weaponInst.setChargedShot(ShotType.FISH_SOULSHOT, false);
		}

		if (isReelingSkill) {
			fish.useRealing(damage, penalty);
		} else {
			fish.usePomping(damage, penalty);
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
