package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import java.util.List;

import net.sf.l2j.gameserver.handler.ISkillHandler;
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
public class Sweep implements ISkillHandler {

	private static final ESkillType[] SKILL_IDS
			= {
				ESkillType.SWEEP
			};

	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets) {
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
	public ESkillType[] getSkillIds() {
		return SKILL_IDS;
	}
}
