/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex;

import org.w3c.dom.Node;

/**
 *
 * @author FinFan
 */
public abstract class StorageTable {

	public abstract void reload();

	protected abstract void load();

	public abstract <T> T get(int identifier);

	public abstract <T> T holder();

	protected final <T> T parseAndGet(Node n, Class<T> type) {
		if (type == int.class) {
			return (T) Integer.valueOf(n.getNodeValue());
		}
		if (type == double.class) {
			return (T) Double.valueOf(n.getNodeValue());
		}
		if (type == float.class) {
			return (T) Float.valueOf(n.getNodeValue());
		}
		if (type == long.class) {
			return (T) Long.valueOf(n.getNodeValue());
		}
		if (type == boolean.class) {
			return (T) Boolean.valueOf(n.getNodeValue());
		}
		return null;
	}

	protected final <T extends Enum<T>> T parseEnum(Node n, Class<T> type) {
		return Enum.valueOf(type, n.getNodeValue());
	}
}
