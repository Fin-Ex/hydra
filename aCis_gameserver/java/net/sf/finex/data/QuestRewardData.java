/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.data;

import lombok.Data;

/**
 *
 * @author FinFan
 */
@Data
public class QuestRewardData {

	private final int id, count;
	private final boolean multipliable;
}
