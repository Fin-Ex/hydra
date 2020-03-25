package net.sf.finex.enums;

import lombok.Getter;
import net.sf.finex.IEnum;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * Crystal Type enumerated.
 *
 * @author Adry_85
 */
public enum EGradeType implements IEnum {
	NG(0, 0, 0, 0, 1, 19),
	D(1, 1458, 11, 90, 20, 39),
	C(2, 1459, 6, 45, 40, 51),
	B(3, 1460, 11, 67, 52, 60),
	A(4, 1461, 19, 144, 61, 75),
	S(5, 1462, 25, 250, 76, 81);

	public static final EGradeType[] VALUES = values();

	private final int _id;
	private final int _crystalId;
	private final int _crystalEnchantBonusArmor;
	private final int _crystalEnchantBonusWeapon;
	@Getter private final int minLevel, maxLevel;

	private EGradeType(int _id, int _crystalId, int _crystalEnchantBonusArmor, int _crystalEnchantBonusWeapon, int minLevel, int maxLevel) {
		this._id = _id;
		this._crystalId = _crystalId;
		this._crystalEnchantBonusArmor = _crystalEnchantBonusArmor;
		this._crystalEnchantBonusWeapon = _crystalEnchantBonusWeapon;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}

	@Override
	public int getId() {
		return _id;
	}

	public int getCrystalId() {
		return _crystalId;
	}

	public int getCrystalEnchantBonusArmor() {
		return _crystalEnchantBonusArmor;
	}

	public int getCrystalEnchantBonusWeapon() {
		return _crystalEnchantBonusWeapon;
	}

	public boolean isGreater(EGradeType crystalType) {
		return getId() > crystalType.getId();
	}

	public boolean isLesser(EGradeType crystalType) {
		return getId() < crystalType.getId();
	}

	public static EGradeType getPlayerGrade(Player player) {
		for (EGradeType grade : values()) {
			if (player.getLevel() >= grade.minLevel && player.getLevel() <= grade.maxLevel) {
				return grade;
			}
		}
		return NG;
	}

	public double getAverageLevel() {
		return (minLevel + maxLevel) / 2.0;
	}

	public String getColoredName() {
		switch (this) {
			case D:
				return "<font color=00FFFF>" + name() + "</font>";
			case C:
				return "<font color=00FF00>" + name() + "</font>";
			case B:
				return "<font color=FF0000>" + name() + "</font>";
			case A:
				return "<font color=C71585>" + name() + "</font>";
			case S:
				return "<font color=FF8C00>" + name() + "</font>";
			default:
				return name();
		}
	}

	@Override
	public String getEnumName() {
		return name();
	}

	@Override
	public String getNormalName() {
		return name() + " - grade";
	}

	@Override
	public int getMask() {
		return 1 << ordinal();
	}
}
