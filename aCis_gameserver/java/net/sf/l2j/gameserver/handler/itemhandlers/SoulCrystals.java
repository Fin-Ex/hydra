package net.sf.l2j.gameserver.handler.itemhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * Template for item skills handler.
 *
 * @author Hasha
 */
public class SoulCrystals implements IItemHandler {

	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
		if (!(playable instanceof Player)) {
			return;
		}

		final EtcItem etcItem = item.getEtcItem();

		final L2Skill itemSkill = etcItem.getStaticSkills().get(0).getSkill();
		if (itemSkill == null) {
			return;
		}

		if (itemSkill.getId() != 2096) {
			return;
		}

		final Player player = (Player) playable;

		if (player.isCastingNow()) {
			return;
		}

		if (!itemSkill.checkCondition(player, player.getTarget(), false)) {
			return;
		}

		// No message on retail, the use is just forgotten.
		if (player.isSkillDisabled(itemSkill)) {
			return;
		}

		player.getAI().setIntention(CtrlIntention.IDLE);
		if (!player.useMagic(itemSkill, forceUse, false)) {
			return;
		}

		int reuseDelay = itemSkill.getReuseDelay();
		if (etcItem.getReuseDelay() > reuseDelay) {
			reuseDelay = etcItem.getReuseDelay();
		}

		player.addTimeStamp(itemSkill, reuseDelay);
		if (reuseDelay != 0) {
			player.disableSkill(itemSkill, reuseDelay);
		}
	}
}
