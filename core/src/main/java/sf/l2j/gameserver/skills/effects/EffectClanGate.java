package sf.l2j.gameserver.skills.effects;

import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.pledge.Clan;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.network.serverpackets.SystemMessage;
import sf.l2j.gameserver.skills.AbnormalEffect;
import sf.l2j.gameserver.skills.Effect;
import sf.l2j.gameserver.skills.EffectTemplate;
import sf.l2j.gameserver.skills.Env;
import sf.l2j.gameserver.skills.L2Effect;
import sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author ZaKaX (Ghost @ L2D)
 */
@Effect("ClanGate")
public class EffectClanGate extends L2Effect {

	public EffectClanGate(Env env, EffectTemplate template) {
		super(env, template);
	}

	@Override
	public boolean onStart() {
		getEffected().startAbnormalEffect(AbnormalEffect.MAGIC_CIRCLE);
		if (getEffected() instanceof Player) {
			Clan clan = ((Player) getEffected()).getClan();
			if (clan != null) {
				SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.COURT_MAGICIAN_CREATED_PORTAL);
				clan.broadcastToOtherOnlineMembers(msg, ((Player) getEffected()));
			}
		}

		return true;
	}

	@Override
	public boolean onActionTime() {
		return false;
	}

	@Override
	public void onExit() {
		getEffected().stopAbnormalEffect(AbnormalEffect.MAGIC_CIRCLE);
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.CLAN_GATE;
	}
}
