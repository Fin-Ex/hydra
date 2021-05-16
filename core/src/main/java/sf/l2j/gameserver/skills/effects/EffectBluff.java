package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Npc;
import sf.l2j.gameserver.model.actor.instance.Folk;
import sf.l2j.gameserver.model.actor.instance.SiegeSummon;
import sf.l2j.gameserver.network.serverpackets.StartRotation;
import sf.l2j.gameserver.network.serverpackets.StopRotation;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

@Effect("Bluff")
public class EffectBluff extends L2Effect {

	public EffectBluff(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BLUFF;
	}

	@Override
	public boolean onStart() {
		if (getEffected() instanceof SiegeSummon || getEffected() instanceof Folk) {
			return false;
		}

		if (getEffected() instanceof Npc) {
			final Npc npc = (Npc) getEffected();
			if (npc.getNpcId() == 35062) {
				return false;
			}

			if (npc.isRaid() || npc.isRaidMinion()) {
				return false;
			}
		}

		getEffected().broadcastPacket(new StartRotation(getEffected().getObjectId(), getEffected().getHeading(), 1, 65535));
		getEffected().broadcastPacket(new StopRotation(getEffected().getObjectId(), getEffector().getHeading(), 65535));
		getEffected().setHeading(getEffector().getHeading());
		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}
}
