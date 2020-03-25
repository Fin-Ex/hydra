/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.classes;

import net.sf.finex.AbstractComponent;
import net.sf.finex.enums.EUIEventType;
import net.sf.finex.interfaces.IUserInterface;
import net.sf.l2j.gameserver.model.WorldObject;

/**
 *
 * @author FinFan
 */
public abstract class AbstractClassComponent extends AbstractComponent implements IUserInterface {

	public AbstractClassComponent(WorldObject worldObject) {
		super(worldObject);
		log.info("{} component created!", getClass().getSimpleName());
	}

	@Override
	public void onAdd() {
	}

	@Override
	public void onRemove() {
	}

	@Override
	public void store() {
	}

	@Override
	public void restore() {
	}

	@Override
	public void delete() {
	}

	@Override
	public void remove(Object... args) {
	}

	@Override
	public void showHtml(EUIEventType event) {
	}
}
