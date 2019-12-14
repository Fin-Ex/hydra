/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import lombok.Getter;
import net.sf.finex.handlers.ISkillMechanic;
import net.sf.finex.handlers.skills.RedirectionSkill;
import net.sf.finex.handlers.skills.ReturnMagic;

/**
 *
 * @author FinFan
 */
public enum ESkillHandlerType {
	NONE(null),
	RETURN_MAGIC(new ReturnMagic()),
	REDIRECTION_SKILL(new RedirectionSkill()),;

	@Getter
	private final ISkillMechanic handler;

	private ESkillHandlerType(ISkillMechanic handler) {
		this.handler = handler;
	}
}
