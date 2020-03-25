package net.sf.l2j.gameserver.handler.itemhandlers;

import lombok.extern.slf4j.Slf4j;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.data.NpcTable;
import net.sf.l2j.gameserver.data.xml.SummonItemData;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.model.L2Spawn;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.instance.ChristmasTree;
import net.sf.l2j.gameserver.model.actor.Pet;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.EItemLocation;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillLaunched;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge;
import net.sf.l2j.gameserver.network.serverpackets.SetupGauge.GaugeColor;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.util.Broadcast;

@Slf4j
public class SummonItems implements IHandler {

	@Override
	public void invoke(Object... args) {
		final Playable playable = (Playable) args[0];
		final ItemInstance item = (ItemInstance) args[1];
		final boolean forceUse = (boolean) args[2];
		if (!(playable instanceof Player)) {
			return;
		}

		final Player activeChar = (Player) playable;

		if (activeChar.isSitting()) {
			activeChar.sendPacket(SystemMessageId.CANT_MOVE_SITTING);
			return;
		}

		if (activeChar.isInObserverMode()) {
			return;
		}

		if (activeChar.isAllSkillsDisabled() || activeChar.isCastingNow()) {
			return;
		}

		final IntIntHolder sitem = SummonItemData.getInstance().getSummonItem(item.getItemId());

		if ((activeChar.getActiveSummon() != null || activeChar.isMounted()) && sitem.getValue() > 0) {
			activeChar.sendPacket(SystemMessageId.SUMMON_ONLY_ONE);
			return;
		}

		if (activeChar.isAttackingNow()) {
			activeChar.sendPacket(SystemMessageId.YOU_CANNOT_SUMMON_IN_COMBAT);
			return;
		}

		final int npcId = sitem.getId();
		if (npcId == 0) {
			return;
		}

		final NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(npcId);
		if (npcTemplate == null) {
			return;
		}

		activeChar.stopMove(null);

		switch (sitem.getValue()) {
			case 0: // static summons (like Christmas tree)
				try {
					for (ChristmasTree ch : activeChar.getKnownTypeInRadius(ChristmasTree.class, 1200)) {
						if (npcTemplate.getNpcId() == ChristmasTree.SPECIAL_TREE_ID) {
							activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANNOT_SUMMON_S1_AGAIN).addCharName(ch));
							return;
						}
					}

					if (activeChar.destroyItem("Summon", item.getObjectId(), 1, null, false)) {
						final L2Spawn spawn = new L2Spawn(npcTemplate);
						spawn.setLoc(activeChar.getPosition());
						spawn.setRespawnState(false);

						final Npc npc = spawn.doSpawn(true);
						npc.setTitle(activeChar.getName());
						npc.setIsRunning(false); // broadcast info
					}
				} catch (Exception e) {
					activeChar.sendPacket(SystemMessageId.TARGET_CANT_FOUND);
				}
				break;
			case 1: // pet summons
				final WorldObject oldTarget = activeChar.getTarget();
				activeChar.setTarget(activeChar);
				Broadcast.toSelfAndKnownPlayers(activeChar, new MagicSkillUse(activeChar, 2046, 1, 5000, 0));
				activeChar.setTarget(oldTarget);
				activeChar.sendPacket(new SetupGauge(GaugeColor.BLUE, 5000));
				activeChar.sendPacket(SystemMessageId.SUMMON_A_PET);
				activeChar.setIsCastingNow(true);

				ThreadPool.schedule(new PetSummonFinalizer(activeChar, npcTemplate, item), 5000);
				break;
			case 2: // wyvern
				activeChar.mount(sitem.getId(), item.getObjectId(), true);
				break;
		}
	}

	// TODO: this should be inside skill handler
	static class PetSummonFinalizer implements Runnable {

		private final Player _activeChar;
		private final ItemInstance _item;
		private final NpcTemplate _npcTemplate;

		PetSummonFinalizer(Player activeChar, NpcTemplate npcTemplate, ItemInstance item) {
			_activeChar = activeChar;
			_npcTemplate = npcTemplate;
			_item = item;
		}

		@Override
		public void run() {
			try {
				_activeChar.sendPacket(new MagicSkillLaunched(_activeChar, 2046, 1));
				_activeChar.setIsCastingNow(false);

				// check for summon item validity
				if (_item == null || _item.getOwnerId() != _activeChar.getObjectId() || _item.getLocation() != EItemLocation.INVENTORY) {
					return;
				}

				// Owner has a pet listed in world.
				if (World.getInstance().getPet(_activeChar.getObjectId()) != null) {
					return;
				}

				// Add the pet instance to world.
				final Pet pet = Pet.restore(_item, _npcTemplate, _activeChar);
				if (pet == null) {
					return;
				}

				World.getInstance().addPet(_activeChar.getObjectId(), pet);

				_activeChar.setPet(pet);

				pet.setRunning();
				pet.setTitle(_activeChar.getName());
				pet.spawnMe();
				pet.startFeed();
				pet.setFollowStatus(true);
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
}
