/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.generator.quest;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;

/**
 *
 * @author FinFan
 */
public class RandomQuestIdFactory {

	@Getter private static final RandomQuestIdFactory instance = new RandomQuestIdFactory();
	@Getter private final AtomicInteger id = new AtomicInteger(0);

	/**
	 * Generate next id for quest.
	 * @return incremenetAndGet (id)
	 */
	public int nextId() {
		if(id.get() >= Integer.MAX_VALUE || id.get() < 0) {
			id.set(0);
		}
		
		return id.incrementAndGet();
	}
}
