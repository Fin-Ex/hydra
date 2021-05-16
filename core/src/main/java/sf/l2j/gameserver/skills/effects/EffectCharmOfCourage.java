package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.EEffectFlag;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author nBd
 */
@Effect("CharmOfCourage")
public class EffectCharmOfCourage extends L2Effect {

	public EffectCharmOfCourage(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.CHARMOFCOURAGE;
	}

	@Override
	public boolean onStart() {
		if (getEffected() instanceof Player) {
			getEffected().broadcastPacket(new EtcStatusUpdate((Player) getEffected()));
			return true;
		}
		return false;
	}

	@Override
	public void onExit() {
		if (getEffected() instanceof Player) {
			getEffected().broadcastPacket(new EtcStatusUpdate((Player) getEffected()));
		}
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public int getEffectFlags() {
		return EEffectFlag.CHARM_OF_COURAGE.getMask();
	}
}
