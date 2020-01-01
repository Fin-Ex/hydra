package net.sf.l2j.gameserver.handler.skillhandlers;


import java.util.List;
import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

/**
 * @author _drunk_
 */
public class Sweep implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.SWEEP
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (!(activeChar instanceof Player)) {
			return;
		}

		final Player player = (Player) activeChar;

		for (WorldObject target : targets) {
			if (!(target instanceof Attackable)) {
				continue;
			}

			final Attackable monster = ((Attackable) target);
			if (!monster.isSpoiled()) {
				continue;
			}

			final List<IntIntHolder> items = monster.getSweepItems();
			if (items.isEmpty()) {
				continue;
			}

			for (IntIntHolder item : items) {
				if (player.isInParty()) {
					player.getParty().distributeItem(player, item, true, monster);
				} else {
					player.addItem("Sweep", item.getId(), item.getValue(), player, true);
				}
			}
			items.clear();
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
