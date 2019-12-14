/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.casting;

import lombok.extern.slf4j.Slf4j;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * Task launching the function useMagic()
 */
@Slf4j
public class QueuedMagicUseTask implements Runnable {

	public Player _currPlayer;
	public L2Skill _queuedSkill;
	public boolean _isCtrlPressed;
	public boolean _isShiftPressed;

	public QueuedMagicUseTask(Player currPlayer, L2Skill queuedSkill, boolean isCtrlPressed, boolean isShiftPressed) {
		_currPlayer = currPlayer;
		_queuedSkill = queuedSkill;
		_isCtrlPressed = isCtrlPressed;
		_isShiftPressed = isShiftPressed;
	}

	@Override
	public void run() {
		try {
			_currPlayer.useMagic(_queuedSkill, _isCtrlPressed, _isShiftPressed);
		} catch (Exception e) {
			log.error("Failed executing QueuedMagicUseTask.", e);
		}
	}

}
