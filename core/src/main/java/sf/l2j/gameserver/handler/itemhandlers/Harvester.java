package sf.l2j.gameserver.handler.itemhandlers;


import sf.l2j.Config;
import sf.l2j.gameserver.data.SkillTable;
import sf.l2j.gameserver.handler.IHandler;
import sf.l2j.gameserver.model.actor.Playable;
import sf.l2j.gameserver.model.actor.instance.Monster;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import sf.l2j.gameserver.network.SystemMessageId;
import sf.l2j.gameserver.skills.L2Skill;

public class Harvester implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!(playable instanceof Player)) {
			return;
		}

		if (!Config.ALLOW_MANOR) {
			return;
		}

		if (!(playable.getTarget() instanceof Monster)) {
			playable.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}

		final Monster _target = (Monster) playable.getTarget();
		if (_target == null || !_target.isDead()) {
			playable.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}

		final L2Skill skill = SkillTable.getInstance().getInfo(2098, 1);
		if (skill != null) {
			playable.useMagic(skill, false, false);
		}
	}
}
