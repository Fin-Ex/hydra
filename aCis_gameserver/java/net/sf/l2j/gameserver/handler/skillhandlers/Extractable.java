package net.sf.l2j.gameserver.handler.skillhandlers;

import lombok.extern.slf4j.Slf4j;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.handler.IHandler;

import net.sf.l2j.gameserver.model.L2ExtractableProductItem;
import net.sf.l2j.gameserver.model.L2ExtractableSkill;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

@Slf4j
public class Extractable implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.EXTRACTABLE,
		ESkillType.EXTRACTABLE_FISH
	};

	@Override
	public void invoke(Object...args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
		if (!(activeChar instanceof Player)) {
			return;
		}

		final L2ExtractableSkill exItem = skill.getExtractableSkill();
		if (exItem == null || exItem.getProductItemsArray().isEmpty()) {
			log.warn("Missing informations for extractable skill id: " + skill.getId() + ".");
			return;
		}

		final Player player = activeChar.getPlayer();
		final int chance = Rnd.get(100000);

		boolean created = false;
		int chanceIndex = 0;

		for (L2ExtractableProductItem expi : exItem.getProductItemsArray()) {
			chanceIndex += (int) (expi.getChance() * 1000);
			if (chance <= chanceIndex) {
				for (IntIntHolder item : expi.getItems()) {
					player.addItem("Extract", item.getId(), item.getValue(), targets[0], true);
				}

				created = true;
				break;
			}
		}

		if (!created) {
			player.sendPacket(SystemMessageId.NOTHING_INSIDE_THAT);
		}
	}

	@Override
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
