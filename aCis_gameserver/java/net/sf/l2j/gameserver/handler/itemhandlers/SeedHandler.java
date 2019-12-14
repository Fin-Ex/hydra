package net.sf.l2j.gameserver.handler.itemhandlers;

import org.slf4j.LoggerFactory;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.MapRegionTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.instancemanager.CastleManorManager;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.manor.Seed;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

public class SeedHandler implements IItemHandler {

	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse) {
		if (!Config.ALLOW_MANOR || !(playable instanceof Player)) {
			return;
		}

		final WorldObject tgt = playable.getTarget();
		if (!(tgt instanceof Attackable) || !((Attackable) tgt).getTemplate().isSeedable()) {
			playable.sendPacket(SystemMessageId.THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING);
			return;
		}

		final Attackable target = (Attackable) tgt;
		if (target.isDead() || target.isSeeded()) {
			playable.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}

		final Seed seed = CastleManorManager.getInstance().getSeed(item.getItemId());
		if (seed == null) {
			return;
		}

		if (seed.getCastleId() != MapRegionTable.getInstance().getAreaCastle(playable.getX(), playable.getY())) {
			playable.sendPacket(SystemMessageId.THIS_SEED_MAY_NOT_BE_SOWN_HERE);
			return;
		}

		target.setSeeded(seed, playable.getObjectId());

		if (item.getItem().hasStaticSkills()) {
			final L2Skill itemSkill = item.getItem().getStaticSkills().get(0).getSkill();
			if (itemSkill == null) {
				return;
			}

			// Key consumption is made on skill call, not on item call.
			playable.useMagic(itemSkill, false, false);
		}
	}
}
