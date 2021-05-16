package sf.l2j.gameserver.handler.skillhandlers;


import sf.finex.handlers.dialog.DlgManager;
import sf.finex.handlers.dialog.requests.ReviveRequest;
import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.model.ShotType;
import sf.l2j.gameserver.model.WorldObject;
import sf.l2j.gameserver.model.actor.Creature;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.skills.Formulas;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.taskmanager.DecayTaskManager;
import sf.l2j.gameserver.templates.skills.ESkillType;

public class Resurrect implements IHandler {

	private static final ESkillType[] SKILL_IDS = {
		ESkillType.RESURRECT
	};

	@Override
	public void invoke(Object... args) {
		final Creature activeChar = (Creature) args[0];
		final L2Skill skill = (L2Skill) args[1];
		final WorldObject[] targets = (WorldObject[]) args[2];
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
	public ESkillType[] commands() {
		return SKILL_IDS;
	}
}
