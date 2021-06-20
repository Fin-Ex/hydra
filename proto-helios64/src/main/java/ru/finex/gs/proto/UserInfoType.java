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
package ru.finex.gs.proto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author finfan
 */
@AllArgsConstructor
public enum UserInfoType implements IUpdateTypeComponent {
	RELATION(0x00, 4),
	BASIC_INFO(0x01, 16),
	BASE_STATS(0x02, 18),
	MAX_HPCPMP(0x03, 14),
	CURRENT_HPMPCP_EXP_SP(0x04, 38),
	ENCHANTLEVEL(0x05, 4),
	APPAREANCE(0x06, 15),
	STATUS(0x07, 6),
	
	STATS(0x08, 56),
	ELEMENTALS(0x09, 14),
	POSITION(0x0A, 18),
	SPEED(0x0B, 18),
	MULTIPLIER(0x0C, 18),
	COL_RADIUS_HEIGHT(0x0D, 18),
	ATK_ELEMENTAL(0x0E, 5),
	CLAN(0x0F, 32),
	
	SOCIAL(0x10, 22),
	VITA_FAME(0x11, 15),
	SLOTS(0x12, 9),
	MOVEMENTS(0x13, 4),
	COLOR(0x14, 10),
	INVENTORY_LIMIT(0x15, 9),
	TRUE_HERO(0x16, 9);
	
	@Getter private final int mask;
	@Getter private final int blockLength;
}