/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.l2j.gameserver.model.item.instance;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zcxv
 */
public class InventoryFlags {

	private final List<InventoryFlag> flags = new ArrayList<>();

	public InventoryFlags() {
	}

	public void addFlag(InventoryFlag flag) {
		flags.add(flag);
	}

	public void removeFlag(InventoryFlag flag) {
		flags.remove(flag);
	}

	public boolean checkAccess(int slot, FlagPolicy policy) {
		for (int i = 0; i < flags.size(); i++) {
			final InventoryFlag flag = flags.get(i);
			if (!flag.checkAccess(slot, policy)) {
				return false;
			}
		}

		return true;
	}

}
