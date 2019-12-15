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

	@Getter @Setter private boolean inDuelMode;

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

	private void onDuelStart(OnDuelStart event) {
		getGameObject().sendMessage("Duel started.");
	}

	private void onDuelEnd(OnDuelEnd event) {
		if (!inDuelMode) {
			return;
		}
		
		if (event.getState() == Duel.DuelState.WINNER && event.getChallenger().getAdena() > PAYMENT) {
			////////////////////////////////// Win
			GladiatorRankTable.getInstance().increment(event.getChallenger(), 3);
		} else {
			////////////////////////////////// Loss
			GladiatorRankTable.getInstance().decrement(event.getChallenger(), 3);
			// reduce adena from challenger
			event.getChallenger().reduceAdena("DuelPayment", PAYMENT, event.getChallenger(), true);
			// give adena to a opponent
			event.getOpponent().getInventory().addAdena("DuelPayment", PAYMENT, event.getOpponent(), event.getChallenger());
			event.getOpponent().sendMessage("You earn 15,000 adena for winning in a Challenge Duel.");
		}

		inDuelMode = false;
		event.getChallenger().sendMessage("Duel mode deactivated.");
		event.getChallenger().getEventBus().unsubscribe(onDuelStart);
		event.getChallenger().getEventBus().unsubscribe(onDuelEnd);
	}

	@Override
	public final Player getGameObject() {
		return super.getGameObject().getPlayer();
	}
}
