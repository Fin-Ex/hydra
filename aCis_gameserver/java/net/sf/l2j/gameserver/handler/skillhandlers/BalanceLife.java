package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author earendil
 */
public class BalanceLife implements ISkillHandler {

	private static final ESkillType[] SKILL_IDS
			= {
				ESkillType.BALANCE_LIFE
			};

	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets) {
		final ISkillHandler handler = SkillHandler.getInstance().getSkillHandler(ESkillType.BUFF);
		if (handler != null) {
			handler.useSkill(activeChar, skill, targets);
		}

		final Player player = activeChar.getPlayer();
		final List<Creature> finalList = new ArrayList<>();

		double fullHP = 0;
		double currentHPs = 0;

		for (WorldObject obj : targets) {
			if (!(obj instanceof Creature)) {
				continue;
			}

			final Creature target = ((Creature) obj);
			if (target.isDead()) {
				continue;
			}

			// Player holding a cursed weapon can't be healed and can't heal
			if (target != activeChar) {
				if (target instanceof Player && ((Player) target).isCursedWeaponEquipped()) {
					continue;
				} else if (player != null && player.isCursedWeaponEquipped()) {
					continue;
				}
			}

			fullHP += target.getMaxHp();
			currentHPs += target.getCurrentHp();

			// Add the character to the final list.
			finalList.add(target);
		}

		if (!finalList.isEmpty()) {
			double percentHP = currentHPs / fullHP;

			for (Creature target : finalList) {
				target.setCurrentHp(target.getMaxHp() * percentHP);

				StatusUpdate su = new StatusUpdate(target);
				su.addAttribute(StatusUpdate.CUR_HP, (int) target.getCurrentHp());
				target.sendPacket(su);
			}
		}
	}

	@Override
	public ESkillType[] getSkillIds() {
		return SKILL_IDS;
	}
}
