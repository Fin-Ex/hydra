package net.sf.l2j.gameserver.handler.skillhandlers;

import org.slf4j.LoggerFactory;

import net.sf.finex.handlers.dialog.DlgManager;
import net.sf.finex.handlers.dialog.requests.ReviveRequest;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;
import net.sf.l2j.gameserver.templates.skills.ESkillType;

public class Resurrect implements ISkillHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.RESURRECT
	};

	@Override
	public void useSkill(Creature activeChar, L2Skill skill, WorldObject[] targets) {
		for (WorldObject cha : targets) {
			final Creature target = (Creature) cha;
			if (activeChar.isPlayer()) {
				if (cha.isPlayer()) {
					DlgManager.getInstance().getRequest(ReviveRequest.class).handle(cha.getPet().getPlayer(), ((Player) activeChar).getPlayer(), skill, false);
				} else if (cha.isPet()) {
					if (cha.getPet().getPlayer() == activeChar) {
						target.doRevive(Formulas.calculateSkillResurrectRestorePercent(skill.getPower(), activeChar));
					} else {
						DlgManager.getInstance().getRequest(ReviveRequest.class).handle(cha.getPet().getPlayer(), activeChar.getPlayer(), skill, true);
					}
				} else {
					target.doRevive(Formulas.calculateSkillResurrectRestorePercent(skill.getPower(), activeChar));
				}
			} else {
				DecayTaskManager.getInstance().cancel(target);
				target.doRevive(Formulas.calculateSkillResurrectRestorePercent(skill.getPower(), activeChar));
			}
		}
		activeChar.setChargedShot(activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOT) ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}

	@Override
	public ESkillType[] getSkillIds() {
		return SKILL_IDS;
	}
}
