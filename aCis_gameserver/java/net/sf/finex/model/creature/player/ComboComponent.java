/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.creature.player;

import lombok.extern.slf4j.Slf4j;
import net.sf.finex.AbstractComponent;
import net.sf.l2j.Config;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 *
 * @author FinFan
 */
@Slf4j
public class ComboComponent extends AbstractComponent {

	private static final int COMBO_TIME = 30_000;
	
	private int value;
	private long time;

	public ComboComponent(WorldObject worldObject) {
		super(worldObject);
	}

	public void update(Creature target) {
		if(getGameObject().getLevel() - target.getLevel() > Config.COMBO_PENALTY) {
			return;
		}
		
		if(time != 0 && System.currentTimeMillis() >= time) {
			stop();
		}
		
		if(value == Short.MAX_VALUE) {
			value = Short.MAX_VALUE;
		} else {
			value++;
		}
		
		getGameObject().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COMBO_BONUS_INCREASE_TO_S1).addNumber(value));
		final int random = Rnd.get(20);
		switch (random) {
			case 0:
				getGameObject().sendPacket(SystemMessageId.COMBO_CONTINUE_1);
				break;
				
			case 10:
				getGameObject().sendPacket(SystemMessageId.COMBO_CONTINUE_2);
				break;
				
			case 20:
				getGameObject().sendPacket(SystemMessageId.COMBO_CONTINUE_3);
				break;
		}
		time = System.currentTimeMillis() + COMBO_TIME;
	}
	
	public void stop() {
		value = 0;
		time = 0;
	}
	
	public double calcExpAndSp() {
		return value / 100. + 1;
	}

	public boolean isActivated() {
		return System.currentTimeMillis() < time;
	}
	
	@Override
	public Player getGameObject() {
		return super.getGameObject().getPlayer();
	}

	@Deprecated
	@Override
	public void onAdd() {
	}

	@Deprecated
	@Override
	public void onRemove() {
	}

	@Deprecated
	@Override
	public void store() {
	}

	@Deprecated
	@Override
	public void restore() {
	}

	@Deprecated
	@Override
	public void delete() {
	}

	@Deprecated
	@Override
	public void remove(Object... args) {
	}
}
