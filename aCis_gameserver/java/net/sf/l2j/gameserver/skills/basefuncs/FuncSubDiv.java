/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.skills.basefuncs;


import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.Stats;

public class FuncSubDiv extends Func {

	public FuncSubDiv(Stats pStat, int pOrder, Object owner, Lambda lambda) {
		super(pStat, pOrder, owner, lambda);
	}

	@Override
	public void calc(Env env) {
		if (cond == null || cond.test(env)) {
			double finalValue = lambda.calc(env);
			if(effectBonus > 0) {
				finalValue *= effectBonus;
			}
			if(skillBonus > 0) {
				finalValue *= skillBonus;
			}
			env.divValue(1 - (finalValue / 100));
		}
	}
}
