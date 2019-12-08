/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.finex.enums;

import lombok.Getter;
import static net.sf.finex.enums.EGradeType.A;
import static net.sf.finex.enums.EGradeType.B;
import static net.sf.finex.enums.EGradeType.C;
import static net.sf.finex.enums.EGradeType.D;
import static net.sf.finex.enums.EGradeType.NG;
import static net.sf.finex.enums.EGradeType.S;

/**
 *
 * @author FinFan
 */
public enum ETownType {
	Dark_Elven_Village(new EGradeType[]{NG}, 2),
	Talking_Island(new EGradeType[]{NG}, 0),
	Elven_Village(new EGradeType[]{NG}, 1),
	Orc_Village(new EGradeType[]{NG}, 3),
	Gludin(new EGradeType[]{NG, D}, 6),
	Dwarven_Village(new EGradeType[]{NG}, 4),
	Gludio(new EGradeType[]{D}, 5),
	Dion(new EGradeType[]{D}, 7),
	Giran(new EGradeType[]{C, B, A}, 8, 12),
	Oren(new EGradeType[]{A, B}, 9),
	Hunter_Village(new EGradeType[]{C, B}, 11),
	Aden(new EGradeType[]{A, S}, 10),
	Goddard(new EGradeType[]{A, S}, 15),
	Rune(new EGradeType[]{A, S}, 14),
	Heine(new EGradeType[]{C, B}, 13),
	Floran(new EGradeType[]{D}, 17),
	Schuttgart(new EGradeType[]{C, B}, 16),
	Primeval_Island(new EGradeType[]{S}, 18);

	public static final ETownType[] VALUES = values();

	@Getter private final EGradeType[] grades;
	@Getter private final int[] mapRegion;

	private ETownType(EGradeType[] grades, int... mapRegion) {
		this.grades = grades;
		this.mapRegion = mapRegion;
	}

	public boolean checkRegion(int region) {
		for (int i = 0; i < mapRegion.length; i++) {
			if (mapRegion[i] == region) {
				return true;
			}
		}

		return false;
	}

	public int getId() {
		return ordinal() + 1;
	}

	public static ETownType getTownById(int id) {
		for (ETownType t : VALUES) {
			if (t.getId() == id) {
				return t;
			}
		}

		return Floran;
	}

	@Override
	public String toString() {
		return name().replace("_", " ");
	}
}
