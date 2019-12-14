package net.sf.l2j.gameserver.skills.effects;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.EEffectFlag;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

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
