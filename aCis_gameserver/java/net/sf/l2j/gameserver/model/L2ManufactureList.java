package net.sf.l2j.gameserver.model;

import org.slf4j.LoggerFactory;

import net.sf.finex.data.ManufactureItemData;
import java.util.ArrayList;
import java.util.List;

public class L2ManufactureList {

	private List<ManufactureItemData> _list;
	private boolean _confirmed;
	private String _manufactureStoreName;

	public L2ManufactureList() {
		_list = new ArrayList<>();
		_confirmed = false;
	}

	public int size() {
		return _list.size();
	}

	public void setConfirmedTrade(boolean x) {
		_confirmed = x;
	}

	public boolean hasConfirmed() {
		return _confirmed;
	}

	/**
	 * @param manufactureStoreName The _manufactureStoreName to set.
	 */
	public void setStoreName(String manufactureStoreName) {
		_manufactureStoreName = manufactureStoreName;
	}

	/**
	 * @return Returns the _manufactureStoreName.
	 */
	public String getStoreName() {
		return _manufactureStoreName;
	}

	public void add(ManufactureItemData item) {
		_list.add(item);
	}

	public List<ManufactureItemData> getList() {
		return _list;
	}

	public void setList(List<ManufactureItemData> list) {
		_list = list;
	}
}
