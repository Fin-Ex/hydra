/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.classes;

import lombok.Getter;
import lombok.Setter;
import net.sf.finex.data.tables.GladiatorRankTable;
import net.sf.finex.events.AbstractEventSubscription;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnDuelEnd;
import net.sf.l2j.gameserver.model.actor.events.OnDuelStart;
import net.sf.l2j.gameserver.model.entity.Duel;

/**
 *
 * @author finfan
 */
public class Gladiator extends AbstractClassComponent {

	private static final int PAYMENT = 15000;

	@Getter
	@Setter
	private boolean inDuelMode;

	private final AbstractEventSubscription<OnDuelStart> onDuelStart;
	private final AbstractEventSubscription<OnDuelEnd> onDuelEnd;

	public Gladiator(Player player) {
		super(player);
		onDuelStart = getGameObject().getEventBus().subscribe().cast(OnDuelStart.class).forEach(this::onDuelStart);
		onDuelEnd = getGameObject().getEventBus().subscribe().cast(OnDuelEnd.class).forEach(this::onDuelEnd);
		if (GladiatorRankTable.getInstance().get(player) == null) {
			GladiatorRankTable.getInstance().insert(player);
		}
	}

	public boolean checkDuel() {
		if (inDuelMode && !getGameObject().reduceAdena("DuelPayment", PAYMENT, getGameObject(), true)) {
			getGameObject().sendMessage("Not enought adena for duel by Challenge.");
			return false;
		}

		return true;
	}

	private void onDuelStart(OnDuelStart event) {
		getGameObject().sendMessage("Duel started.");
	}

	private void onDuelEnd(OnDuelEnd event) {
		if (event.getState() == Duel.DuelState.WINNER) {
			////////////////////////////////// Win
			GladiatorRankTable.getInstance().increment(getGameObject(), 3);
			// return adena for this dueling
			getGameObject().getInventory().addAdena("DuelPayment", PAYMENT, getGameObject(), null);
		} else {
			////////////////////////////////// Loss
			GladiatorRankTable.getInstance().decrement(getGameObject(), 3);
			// give adena to a opponent
			event.getOpponent().getInventory().addAdena("DuelPayment", PAYMENT, event.getOpponent(), getGameObject());
		}

		inDuelMode = false;
		getGameObject().sendMessage("Duel mode deactivated.");
		getGameObject().getEventBus().unsubscribe(onDuelStart);
		getGameObject().getEventBus().unsubscribe(onDuelEnd);
	}

	@Override
	public final Player getGameObject() {
		return super.getGameObject().getPlayer();
	}
}
