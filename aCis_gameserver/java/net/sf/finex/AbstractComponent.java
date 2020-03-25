/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex;

import net.sf.finex.interfaces.IPersistence;
import net.sf.l2j.gameserver.model.WorldObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author FinFan
 */
public abstract class AbstractComponent implements IPersistence {

	protected static final Logger log = LoggerFactory.getLogger(AbstractComponent.class);
	private final WorldObject gameObject;

	public AbstractComponent(WorldObject worldObject) {
		this.gameObject = worldObject;
	}

	public abstract void onAdd();

	public abstract void onRemove();

	public WorldObject getGameObject() {
		return gameObject;
	}

	public boolean isType(Class<?> type) {
		return getClass().isInstance(type);
	}

	public boolean isChildOf(Class<? extends AbstractComponent> type) {
		return getClass().isAssignableFrom(type);
	}
}
