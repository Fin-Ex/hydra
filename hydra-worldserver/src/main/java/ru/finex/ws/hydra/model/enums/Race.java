/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ru.finex.ws.hydra.model.enums;

import lombok.Getter;

/**
 * Creature races enumerated.
 * @author Zealar
 */
@Getter
public enum Race implements IdEnum {
	HUMAN,
	ELF,
	DARK_ELF,
	ORC,
	DWARF,
	KAMAEL,
	ERTHEIA,
	ANIMAL,
	BEAST,
	BUG,
	CASTLE_GUARD,
	CONSTRUCT,
	DEMONIC,
	DIVINE,
	DRAGON,
	ELEMENTAL,
	ETC,
	FAIRY,
	GIANT,
	HUMANOID,
	MERCENARY,
	NONE,
	PLANT,
	SIEGE_WEAPON,
	UNDEAD,
	FRIEND; // FRIEND ordinal has to be confirmed

	@Override
	public int getId() {
		return ordinal();
	}

	public static Race ofId(int id) {
		return values()[id];
	}
}
