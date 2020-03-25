package net.sf.l2j.gameserver.model.itemcontainer.listeners;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.serverpackets.SkillCoolTime;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ItemPassiveSkillsListener implements OnEquipListener {

	private static ItemPassiveSkillsListener instance = new ItemPassiveSkillsListener();

	public static ItemPassiveSkillsListener getInstance() {
		return instance;
	}

	@Override
	public void onEquip(int slot, ItemInstance instance, Playable actor) {
		final Player player = (Player) actor;
		final Item item = instance.getItem();

		boolean update = false;
		boolean updateTimeStamp = false;

		if (item instanceof Weapon) {
			// Apply augmentation bonuses on equip
			if (instance.isAugmented()) {
				instance.getAugmentation().applyBonus(player);
			}

			// Verify if the grade penalty is occuring. If yes, then forget +4 dual skills and SA attached to weapon.
			if (player.getExpertiseIndex() < item.getCrystalType().getId()) {
				return;
			}

			// Add skills bestowed from +4 Duals
			if (instance.getEnchantLevel() >= 4) {
				final L2Skill enchant4Skill = ((Weapon) item).getEnchant4Skill();
				if (enchant4Skill != null) {
					player.addSkill(enchant4Skill, false);
					update = true;
				}
			}
		}

		// Collect all having skills by this item
		final List<L2Skill> skills = new ArrayList<>();
		if (item.hasDynamicSkills()) {
			item.getDynamicSkills().forEach(holder -> skills.add(SkillTable.getInstance().getLevelFrom(holder.getId(), actor.getLevel())));
		}

		if (item.hasStaticSkills()) {
			item.getStaticSkills().forEach(holder -> skills.add(holder.getSkill()));
		}

		// handle all skills giving by this item
		if (!skills.isEmpty()) {
			for (L2Skill itemSkill : skills) {
				if (itemSkill != null) {
					player.addSkill(itemSkill, false);

					if (itemSkill.isActive()) {
						if (!player.getReuseTimeStamp().containsKey(itemSkill.getReuseHashCode())) {
							final int equipDelay = itemSkill.getEquipDelay();
							if (equipDelay > 0) {
								player.addTimeStamp(itemSkill, equipDelay);
								player.disableSkill(itemSkill, equipDelay);
							}
						}
						updateTimeStamp = true;
					}
					update = true;
				}
			}

			if (update) {
				player.sendSkillList();

				if (updateTimeStamp) {
					player.sendPacket(new SkillCoolTime(player));
				}
			}
		}
	}

	@Override
	public void onUnequip(int slot, ItemInstance instance, Playable actor) {
		final Player player = (Player) actor;
		final Item item = instance.getItem();

		boolean update = false;

		if (item instanceof Weapon) {
			// Remove augmentation bonuses on unequip
			if (instance.isAugmented()) {
				instance.getAugmentation().removeBonus(player);
			}

			// Remove skills bestowed from +4 Duals
			if (instance.getEnchantLevel() >= 4) {
				final L2Skill enchant4Skill = ((Weapon) item).getEnchant4Skill();
				if (enchant4Skill != null) {
					player.removeSkill(enchant4Skill, false, enchant4Skill.isPassive());
					update = true;
				}
			}
		}

		if (item.hasSkills()) {
			final List<L2Skill> skills = new ArrayList<>();
			if (item.hasDynamicSkills()) {
				item.getDynamicSkills().forEach(holder -> skills.add(holder.getSkill()));
			}

			if (item.hasStaticSkills()) {
				item.getStaticSkills().forEach(holder -> skills.add(holder.getSkill()));
			}

			if (!skills.isEmpty()) {
				for (L2Skill itemSkill : skills) {
					if (itemSkill != null) {
						boolean found = false;

						for (ItemInstance pItem : player.getInventory().getPaperdollItems()) {
							if (pItem != null && item.getItemId() == pItem.getItemId()) {
								found = true;
								break;
							}
						}

						if (!found) {
							player.removeSkill(itemSkill, false, itemSkill.isPassive());
							update = true;
						}
					}
				}
				if (update) {
					player.sendSkillList();
				}
			}
		}
	}
}
