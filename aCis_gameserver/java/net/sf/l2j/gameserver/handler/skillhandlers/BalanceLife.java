package net.sf.l2j.gameserver.handler.skillhandlers;


import java.util.ArrayList;
import java.util.List;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author earendil
 */
public class BalanceLife implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.BALANCE_LIFE
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		
		final IHandler handler = HandlerTable.getInstance().get(Continuous.class);
		if (handler != null) {
			handler.invoke(activeChar, skill, targets);
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
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
	
}
