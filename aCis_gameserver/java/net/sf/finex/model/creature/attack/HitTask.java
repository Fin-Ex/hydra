/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.creature.attack;

/**
 * Task lauching the function onHitTimer().<BR>
 * <BR>
 * <B><U> Actions</U> :</B>
 * <ul>
 * <li>If the attacker/target is dead or use fake death, notify the AI with
 * EVT_CANCEL and send ActionFailed (if attacker is a Player)</li>
 * <li>If attack isn't aborted, send a message system (critical hit, missed...)
 * to attacker/target if they are Player</li>
 * <li>If attack isn't aborted and hit isn't missed, reduce HP of the target and
 * calculate reflection damage to reduce HP of attacker if necessary</li>
 * <li>if attack isn't aborted and hit isn't missed, manage attack or cast break
 * of the target (calculating rate, sending message...)</li>
 * </ul>
 */
public class HitTask implements Runnable {

	public final AbstractHit hit;
	public final DamageInfo info;
	public final boolean soulshot;

	public HitTask(AbstractHit hit, DamageInfo info, boolean soulshot) {
		this.hit = hit;
		this.info = info;
		this.soulshot = soulshot;
	}

	@Override
	public void run() {
		hit.hit(info, soulshot);
	}

}
