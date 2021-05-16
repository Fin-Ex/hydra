package sf.finex.model.talents.effects;

import sf.finex.events.AbstractEventSubscription;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.actor.events.OnSuccessParry;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SkillCoolTime;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.skills.L2Skill;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * Мнгновенно откатывает после парирования.
 *
 * @author zcxv
 * @date 19.05.2019
 */
@Effect("TripleSlasher")
public class TripleSlasher extends L2Effect {

	private AbstractEventSubscription<OnSuccessParry> eventSubsribtion;

	public TripleSlasher(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public boolean onStart() {
		if (!getEffector().isPlayer()) {
			return false;
		}

		eventSubsribtion = getEffector().getEventBus().subscribe()
				.cast(OnSuccessParry.class)
				.forEach(this::onParry);

		return super.onStart();
	}

	@Override
	public void onExit() {
		getEffector().getEventBus().unsubscribe(eventSubsribtion);
		super.onExit();
	}

	private void onParry(OnSuccessParry e) {
		final Player player = getEffector().getPlayer();

		if (player.isDead()) {
			return;
		}

		final L2Skill tripleSlash = player.getSkill(1);
		if (tripleSlash == null || !player.isSkillDisabled(tripleSlash)) {
			return;
		}

		player.getReuseTimeStamp().remove(tripleSlash.getReuseHashCode());
		player.enableSkill(tripleSlash);
		player.sendPacket(new SkillCoolTime(player));
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_SKILL_COOLDOWN_WAS_REMOVED).addSkillName(tripleSlash));
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BUFF;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

}
