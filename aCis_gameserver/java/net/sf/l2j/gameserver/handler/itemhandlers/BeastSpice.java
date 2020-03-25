package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.FeedableBeast;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

public class BeastSpice implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];

		if (!(playable instanceof Player)) {
			return;
		}

		Player activeChar = (Player) playable;

		if (!(activeChar.getTarget() instanceof FeedableBeast)) {
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}

		int skillId = 0;
		switch (item.getItemId()) {
			case 6643:
				skillId = 2188;
				break;
			case 6644:
				skillId = 2189;
				break;
		}

		L2Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
		if (skill != null) {
			activeChar.useMagic(skill, false, false);
		}
	}
}
