/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.classes;

import java.util.concurrent.TimeUnit;
import net.sf.finex.events.AbstractEventSubscription;
import net.sf.finex.handlers.talents.SecondWind;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.events.OnReduceHp;

/**
 *
 * @author finfan
 */
public class Warlord extends AbstractClassComponent {
	
	private long secondWindTimeStamp = 0;
	
	private final AbstractEventSubscription<OnReduceHp> onReduceHp;
	
	public Warlord(Player player) {
		super(player);
		onReduceHp = player.getEventBus().subscribe().cast(OnReduceHp.class).forEach(this::onReduceHp);
	}

	public void setSecondWindTimeStamp(int seconds) {
		this.secondWindTimeStamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
	}
	
	public boolean hasSecondWindTimeStamp() {
		return secondWindTimeStamp > 0 && secondWindTimeStamp > System.currentTimeMillis();
	}

	@Override
	public Player getGameObject() {
		return super.getGameObject().getPlayer();
	}

	@Override
	public void onRemove() {
		getGameObject().getEventBus().unsubscribe(onReduceHp);
	}
	
	private void onReduceHp(OnReduceHp event) {
		if(SecondWind.validate(getGameObject())) {
			log.info("Trying to call talent Second Wind talent.");
			SkillTable.FrequentTalent.SECOND_WIND.getHandler().invoke(event.getVictim());
		}
	}
}
