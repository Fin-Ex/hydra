package sf.l2j.gameserver.model.item.kind;

import sf.l2j.gameserver.model.item.type.ArmorType;
import sf.l2j.gameserver.templates.StatsSet;

/**
 * This class is dedicated to the management of armors.
 */
public final class Armor extends Item {

	private ArmorType _type;

	/**
	 * Constructor for Armor.<BR>
	 * <BR>
	 * <U><I>Variables filled :</I></U><BR>
	 * <LI>_avoidModifier</LI>
	 * <LI>_pDef & _mDef</LI>
	 * <LI>_mpBonus & _hpBonus</LI>
	 *
	 * @param set : StatsSet designating the set of couples (key,value)
	 * caracterizing the armor
	 * @see Item constructor
	 */
	public Armor(StatsSet set) {
		super(set);
		_type = ArmorType.valueOf(set.getString("armor_type", "none").toUpperCase());
		if (_type == ArmorType.NONE && getBodyPart() == SLOT_L_HAND) // retail define shield as NONE
		{
			_type = ArmorType.SHIELD;
		}

		_type1 = TYPE1_SHIELD_ARMOR;
		_type2 = TYPE2_SHIELD_ARMOR;
	}

	/**
	 * Returns the type of the armor.
	 *
	 * @return ArmorType
	 */
	@Override
	public ArmorType getItemType() {
		return _type;
	}

	/**
	 * Returns the ID of the item after applying the mask.
	 *
	 * @return int : ID of the item
	 */
	@Override
	public final int getItemMask() {
		return getItemType().mask();
	}
}
