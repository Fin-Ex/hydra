package net.sf.l2j.gameserver.network.clientpackets;

import org.slf4j.LoggerFactory;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.CharTemplateTable;
import net.sf.l2j.gameserver.data.PlayerNameTable;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.SkillTreeTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.base.Sex;
import net.sf.l2j.gameserver.model.item.instance.type.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.serverpackets.CharCreateFail;
import net.sf.l2j.gameserver.network.serverpackets.CharCreateOk;
import net.sf.l2j.gameserver.network.serverpackets.CharSelectInfo;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.ScriptManager;

@SuppressWarnings("unused")
public final class CharacterCreate extends L2GameClientPacket {

	// cSdddddddddddd
	private String _name;
	private int _race;
	private byte _sex;
	private int _classId;
	private int _int;
	private int _str;
	private int _con;
	private int _men;
	private int _dex;
	private int _wit;
	private byte _hairStyle;
	private byte _hairColor;
	private byte _face;

	@Override
	protected void readImpl() {
		_name = readS();
		_race = readD();
		_sex = (byte) readD();
		_classId = readD();
		_int = readD();
		_str = readD();
		_con = readD();
		_men = readD();
		_dex = readD();
		_wit = readD();
		_hairStyle = (byte) readD();
		_hairColor = (byte) readD();
		_face = (byte) readD();
	}

	@Override
	protected void runImpl() {
		if (!StringUtil.isValidPlayerName(_name)) {
			sendPacket(new CharCreateFail((_name.length() > 16) ? CharCreateFail.REASON_16_ENG_CHARS : CharCreateFail.REASON_INCORRECT_NAME));
			return;
		}

		if (_face > 2 || _face < 0) {
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}

		if (_hairStyle < 0 || (_sex == 0 && _hairStyle > 4) || (_sex != 0 && _hairStyle > 6)) {
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}

		if (_hairColor > 3 || _hairColor < 0) {
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}

		if (PlayerNameTable.getInstance().getCharactersInAcc(getClient().getAccountName()) >= 7) {
			sendPacket(new CharCreateFail(CharCreateFail.REASON_TOO_MANY_CHARACTERS));
			return;
		}

		if (PlayerNameTable.getInstance().getPlayerObjectId(_name) > 0) {
			sendPacket(new CharCreateFail(CharCreateFail.REASON_NAME_ALREADY_EXISTS));
			return;
		}

		final PlayerTemplate template = CharTemplateTable.getInstance().getTemplate(_classId);
		if (template == null || template.getClassBaseLevel() > 1) {
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}

		final Player newChar = Player.create(IdFactory.getInstance().getNextId(), template, getClient().getAccountName(), _name, _hairStyle, _hairColor, _face, Sex.values()[_sex]);
		if (newChar == null) {
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}

		newChar.setCurrentCp(0);
		newChar.setCurrentHp(newChar.getMaxHp());
		newChar.setCurrentMp(newChar.getMaxMp());

		// send acknowledgement
		sendPacket(CharCreateOk.STATIC_PACKET);

		World.getInstance().addObject(newChar);

		newChar.getPosition().set(template.getSpawn());
		newChar.setTitle("");

		newChar.registerShortCut(new L2ShortCut(0, 0, 3, 2, -1, 1)); // attack shortcut
		newChar.registerShortCut(new L2ShortCut(3, 0, 3, 5, -1, 1)); // take shortcut
		newChar.registerShortCut(new L2ShortCut(10, 0, 3, 0, -1, 1)); // sit shortcut

		for (Item ia : template.getItems()) {
			ItemInstance item = newChar.getInventory().addItem("Init", ia.getItemId(), 1, newChar, null);
			if (item.getItemId() == 5588) // tutorial book shortcut
			{
				newChar.registerShortCut(new L2ShortCut(11, 0, 1, item.getObjectId(), -1, 1));
			}

			if (item.isEquipable()) {
				if (newChar.getActiveWeaponItem() == null || !(item.getItem().getType2() != Item.TYPE2_WEAPON)) {
					newChar.getInventory().equipItemAndRecord(item);
				}
			}
		}

		for (L2SkillLearn skill : SkillTreeTable.getInstance().getAvailableSkills(newChar, newChar.getClassId())) {
			newChar.addSkill(SkillTable.getInstance().getInfo(skill.getId(), skill.getLevel()), true);
			if (skill.getId() == 1001 || skill.getId() == 1177) {
				newChar.registerShortCut(new L2ShortCut(1, 0, 2, skill.getId(), 1, 1));
			}

			if (skill.getId() == 1216) {
				newChar.registerShortCut(new L2ShortCut(9, 0, 2, skill.getId(), 1, 1));
			}
		}

		if (!Config.DISABLE_TUTORIAL) {
			if (newChar.getQuestState("Tutorial") == null) {
				Quest q = ScriptManager.getInstance().getQuest("Tutorial");
				if (q != null) {
					q.newQuestState(newChar).setState(Quest.STATE_STARTED);
				}
			}
		}

		newChar.setOnlineStatus(true, false);
		newChar.deleteMe();

		final CharSelectInfo cl = new CharSelectInfo(getClient().getAccountName(), getClient().getSessionId().playOkID1);
		getClient().getConnection().sendPacket(cl);
		getClient().setCharSelection(cl.getCharInfo());
	}
}
