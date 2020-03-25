/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import lombok.Getter;

/**
 *
 * @author finfan
 */
public enum EStage {
	REGISTER(new StageRegistration()),
	INSTRUCTING(new StageInstructing()),
	START(new StageStart()),
	FINISH(new StageFinish());
	
	@Getter private final IStageHandler handler;

	private EStage(IStageHandler handler) {
		this.handler = handler;
	}
}
