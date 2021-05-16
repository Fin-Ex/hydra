/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.l2j.gameserver.model.actor.template;

import lombok.Getter;
import sf.l2j.gameserver.skills.Stats;

/**
 *
 * @author FinFan
 */
public enum Race {
	UNKNOWN,
	UNDEAD(Stats.PAtkUndeads, Stats.PDefUndeads),
	MAGICCREATURE(Stats.PAtkMagicCreatures, Stats.PDefMagicCreatures),
	BEAST(Stats.PAtkMonsters, Stats.PDefMonsters),
	ANIMAL(Stats.PAtkAnimals, Stats.PDefAnimals),
	PLANT(Stats.PAtkPlants, Stats.PDefPlants),
	HUMANOID,
	SPIRIT(Stats.PAtkSpirits, Stats.PDefSpirits),
	ANGEL(Stats.PAtkAngels, Stats.PDefAngels),
	DEMON(Stats.PAtkDemons, Stats.PDefDemons),
	DRAGON(Stats.PAtkDragons, Stats.PDefDragons),
	GIANT(Stats.PAtkGiants, Stats.PDefGiants),
	BUG(Stats.PAtkInsects, Stats.PDefInsects),
	FAIRIE(Stats.PAtkFairies, Stats.PDefFairies),
	HUMAN,
	ELVE,
	DARKELVE,
	ORC,
	DWARVE,
	OTHER,
	NONLIVING,
	SIEGEWEAPON,
	DEFENDINGARMY,
	MERCENARIE;

	public static final Race[] VALUES = values();

	@Getter
	private final Stats PAtkStat, PDefStat;

	private Race(Stats PAtkStat, Stats PDefStat) {
		this.PAtkStat = PAtkStat;
		this.PDefStat = PDefStat;
	}

	private Race() {
		this.PAtkStat = null;
		this.PDefStat = null;
	}
}
