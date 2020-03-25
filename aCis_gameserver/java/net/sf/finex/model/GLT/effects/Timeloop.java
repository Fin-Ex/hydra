/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT.effects;

import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.skills.Effect;
import net.sf.l2j.gameserver.skills.EffectTemplate;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.L2Effect;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 *
 * @author finfan
 */
@Effect("Timeloop")
public class Timeloop extends L2Effect {
	
	private final Location startPosition;
			
	public Timeloop(Env env, EffectTemplate template, Location startPosition) {
		super(env, template);
		this.startPosition = _effected.getPosition();
	}

	@Override
	public L2EffectType getEffectType() {
		return L2EffectType.BETRAY;
	}

	@Override
	public boolean onStart() {
		return super.onStart();
	}

	@Override
	public boolean onActionTime() {
		return true;
	}

	@Override
	public void onExit() {
		if(startPosition != null) {
			_effected.teleToLocation(startPosition, 10);
		}
		super.onExit();
	}
}
