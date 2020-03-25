/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.movie.actions;

import lombok.extern.slf4j.Slf4j;
import net.sf.finex.enums.EMovieAction;
import net.sf.finex.model.movie.EItemManipulation;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;

/**
 *
 * @author finfan
 */
@Slf4j
public class ActItem extends AbstractActorAction {

	private final EItemManipulation type;
	private ItemInstance item;
	private int[] itemIdAndCount;

	public ActItem(EItemManipulation type, Creature actor, WorldObject target) {
		super(actor, target);
		this.type = type;
	}

	@Override
	public void call() {
		if (!target.isPlayer()) {
			log.warn("ActItem: trying to add item to NON playable character.");
			return;
		}

		final Player targetPlayer = target.getPlayer();
		// Give item to player
		if (type == EItemManipulation.GIVE) {
			if (item != null) {
				targetPlayer.addItem("ActItem", item, actor, true);
			} else if (itemIdAndCount != null) {
				targetPlayer.addItem("ActItem", itemIdAndCount[0], itemIdAndCount[1], actor, true);
			}
		} 
		// Take item from player
		else {
			if (!actor.isPlayer()) {
				log.warn("ActItem: giver cant be a NON player instance.");
				return;
			}

			final Player actorPlayer = actor.getPlayer();
			if (item != null) {
				actorPlayer.destroyItem("ActItem", item, targetPlayer, true);
			} else if (itemIdAndCount != null) {
				actorPlayer.destroyItem("ActItem", itemIdAndCount[0], itemIdAndCount[1], targetPlayer, true);
			}
		}
	}

	public ActItem setItemIdAndCount(int[] itemIdAndCount) {
		this.itemIdAndCount = itemIdAndCount;
		return this;
	}

	public ActItem setItem(ItemInstance item) {
		this.item = item;
		return this;
	}

	@Override
	public EMovieAction getType() {
		return EMovieAction.ADD_ITEM;
	}

	@Override
	public long getTiming() {
		return 700;
	}

}
