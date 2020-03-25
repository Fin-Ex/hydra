package net.sf.l2j.gameserver.skills.effects;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.network.serverpackets.DeleteObject;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.skills.AbnormalEffect;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Invisible")
public class EffectInvisible extends L2Effect {

	public EffectInvisible(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.INVISIBLE;
	}

	@Override
	public boolean onStart() {
		if (!getEffected().isPlayer() || getEffected().getPlayer().getAppearance().isInvisible()) {
			return false;
		}

		final Player targetPlayer = getEffected().getPlayer();
		targetPlayer.getAppearance().setInvisible();

		if (targetPlayer.getAI().getNextIntention() != null && targetPlayer.getAI().getNextIntention().getIntention() == CtrlIntention.ATTACK) {
			targetPlayer.getAI().setIntention(CtrlIntention.IDLE);
		}

		final L2GameServerPacket del = new DeleteObject(targetPlayer);
		for (Creature nearCreatures : targetPlayer.getKnownType(Creature.class)) {
			if (nearCreatures.getTarget() == targetPlayer) {
				nearCreatures.removeTarget();
			}

			if (nearCreatures.isPlayer()) {
				nearCreatures.sendPacket(del);
			}
		}
		return super.onStart();
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public void onExit() {
		final Player targetPlayer = getEffected().getPlayer();
		targetPlayer.getAppearance().setVisible();
		targetPlayer.stopAbnormalEffect(AbnormalEffect.STEALTH);
		super.onExit();
	}
}
