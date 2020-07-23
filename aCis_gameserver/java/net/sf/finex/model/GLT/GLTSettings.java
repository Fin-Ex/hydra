/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.model.GLT;

import net.sf.l2j.gameserver.model.base.ClassId;

/**
 * All these settings cheks in {@link net.sf.finex.model.GLT.GLTArbitrator}
 *
 * @author finfan
 */
public class GLTSettings {

	public static final boolean ACTIVATED = false;
	public static final int ZONE_LEAVE_SAFETY_TIME = 90;
	public static final int ADENA_CONTRIBUTION = 200_000; //200k default
	public static final int MAX_PARTICIPANTS = 4; //doesnt have a limit
	public static final int MIN_PARTICIPANTS = 2; //condition case when GLT not change his stage to INSTRUCTING if participants less than @MIN_PARTICIPANTS
	public static final int MIN_LEVEL = -1; //turned off
	public static final int MAX_LEVEL = -1; //turned off
	public static final int LIMIT_WEAPON_ENCHANT = -1; //turned off
	public static final int LIMIT_ARMOR_ENCHANT = -1; //turned off
	public static final int LIMIT_JEWEL_ENCHANT = -1; //turned off
	public static final ClassId[] RESTRICTED_CLASSES = null; //turned off
	public static final int RESTRICTED_CLASS_LEVEL = -1; //turned off, if setted to 1 so classes with 1 level: Warrior, Rogue etc and lower - cant participate
}
