/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data;

import lombok.Data;

/**
 *
 * @author finfan
 */
@Data
public class TimeStamp {

	private int skillId;
	private int skillLvl;
	private long reuse;
	private long stamp;

	public long getRemaining() {
		return Math.max(stamp - System.currentTimeMillis(), 0);
	}

	public boolean hasNotPassed() {
		return System.currentTimeMillis() < stamp;
	}
}
