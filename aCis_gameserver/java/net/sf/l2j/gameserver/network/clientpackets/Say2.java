package net.sf.l2j.gameserver.network.clientpackets;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import lombok.extern.slf4j.Slf4j;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.handler.HandlerTable;
import net.sf.l2j.gameserver.handler.IHandler;
import net.sf.l2j.gameserver.handler.chathandlers.ChatAll;
import net.sf.l2j.gameserver.handler.chathandlers.ChatAlliance;
import net.sf.l2j.gameserver.handler.chathandlers.ChatClan;
import net.sf.l2j.gameserver.handler.chathandlers.ChatHeroVoice;
import net.sf.l2j.gameserver.handler.chathandlers.ChatParty;
import net.sf.l2j.gameserver.handler.chathandlers.ChatPartyMatchRoom;
import net.sf.l2j.gameserver.handler.chathandlers.ChatPartyRoomAll;
import net.sf.l2j.gameserver.handler.chathandlers.ChatPartyRoomCommander;
import net.sf.l2j.gameserver.handler.chathandlers.ChatPetition;
import net.sf.l2j.gameserver.handler.chathandlers.ChatShout;
import net.sf.l2j.gameserver.handler.chathandlers.ChatTell;
import net.sf.l2j.gameserver.handler.chathandlers.ChatTrade;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public final class Say2 extends L2GameClientPacket {

	private static final Logger CHAT_LOG = LoggerFactory.getLogger("chat");

	public static final int ALL = 0;
	public static final int SHOUT = 1; // !
	public static final int TELL = 2;
	public static final int PARTY = 3; // #
	public static final int CLAN = 4; // @
	public static final int GM = 5;
	public static final int PETITION_PLAYER = 6;
	public static final int PETITION_GM = 7;
	public static final int TRADE = 8; // +
	public static final int ALLIANCE = 9; // $
	public static final int ANNOUNCEMENT = 10;
	public static final int BOAT = 11;
	public static final int L2FRIEND = 12;
	public static final int MSNCHAT = 13;
	public static final int PARTYMATCH_ROOM = 14;
	public static final int PARTYROOM_COMMANDER = 15; // (Yellow)
	public static final int PARTYROOM_ALL = 16; // (Red)
	public static final int HERO_VOICE = 17;
	public static final int CRITICAL_ANNOUNCE = 18;

	private static final String[] CHAT_NAMES = {
		"ALL",
		"SHOUT",
		"TELL",
		"PARTY",
		"CLAN",
		"GM",
		"PETITION_PLAYER",
		"PETITION_GM",
		"TRADE",
		"ALLIANCE",
		"ANNOUNCEMENT", // 10
		"BOAT",
		"WILLCRASHCLIENT:)",
		"FAKEALL?",
		"PARTYMATCH_ROOM",
		"PARTYROOM_COMMANDER",
		"PARTYROOM_ALL",
		"HERO_VOICE",
		"CRITICAL_ANNOUNCEMENT"
	};

	private static final String[] WALKER_COMMAND_LIST = {
		"USESKILL",
		"USEITEM",
		"BUYITEM",
		"SELLITEM",
		"SAVEITEM",
		"LOADITEM",
		"MSG",
		"DELAY",
		"LABEL",
		"JMP",
		"CALL",
		"RETURN",
		"MOVETO",
		"NPCSEL",
		"NPCDLG",
		"DLGSEL",
		"CHARSTATUS",
		"POSOUTRANGE",
		"POSINRANGE",
		"GOHOME",
		"SAY",
		"EXIT",
		"PAUSE",
		"STRINDLG",
		"STRNOTINDLG",
		"CHANGEWAITTYPE",
		"FORCEATTACK",
		"ISMEMBER",
		"REQUESTJOINPARTY",
		"REQUESTOUTPARTY",
		"QUITPARTY",
		"MEMBERSTATUS",
		"CHARBUFFS",
		"ITEMCOUNT",
		"FOLLOWTELEPORT"
	};

	private String _text;
	private int _type;
	private String _target;

	@Override
	protected void readImpl() {
		_text = readS();
		_type = readD();
		_target = (_type == TELL) ? readS() : null;
	}

	@Override
	protected void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		if (_type < 0 || _type >= CHAT_NAMES.length) {
			_log.warn("Say2: Invalid type: " + _type + " Player : " + activeChar.getName() + " text: " + _text);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			activeChar.logout();
			return;
		}

		if (_text.isEmpty()) {
			_log.warn(activeChar.getName() + ": sending empty text. Possible packet hack.");
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			activeChar.logout();
			return;
		}

		if (_text.length() >= 100) {
			return;
		}

		if (Config.L2WALKER_PROTECTION && _type == TELL && checkBot(_text)) {
			return;
		}

		if (!activeChar.isGM() && _type == ANNOUNCEMENT) {
			_log.warn(activeChar.getName() + " tried to use announcements without GM statut.");
			return;
		}

		if (activeChar.isChatBanned() || (activeChar.isInJail() && !activeChar.isGM())) {
			activeChar.sendPacket(SystemMessageId.CHATTING_PROHIBITED);
			return;
		}

		if (_type == PETITION_PLAYER && activeChar.isGM()) {
			_type = PETITION_GM;
		}

		if (Config.LOG_CHAT) {
			LogRecord record = new LogRecord(Level.INFO, _text);
			record.setLoggerName("chat");

			if (_type == TELL) {
				CHAT_LOG.info("{} -> {}: {}", activeChar, _target, _text);
			} else {
				CHAT_LOG.info("{} {}: {}", CHAT_NAMES[_type], activeChar, _text);
			}
		}

		_text = _text.replaceAll("\\\\n", "");

		IHandler handler;
		switch (_type) {
			case ALL:
				handler = HandlerTable.getInstance().get(ChatAll.class);
				break;
			case ALLIANCE:
				handler = HandlerTable.getInstance().get(ChatAlliance.class);
				break;
			case CLAN:
				handler = HandlerTable.getInstance().get(ChatClan.class);
				break;
			case HERO_VOICE:
				handler = HandlerTable.getInstance().get(ChatHeroVoice.class);
				break;
			case PARTY:
				handler = HandlerTable.getInstance().get(ChatParty.class);
				break;
			case PARTYMATCH_ROOM:
				handler = HandlerTable.getInstance().get(ChatPartyMatchRoom.class);
				break;
			case PARTYROOM_ALL:
				handler = HandlerTable.getInstance().get(ChatPartyRoomAll.class);
				break;
			case PARTYROOM_COMMANDER:
				handler = HandlerTable.getInstance().get(ChatPartyRoomCommander.class);
				break;
			case PETITION_GM:
			case PETITION_PLAYER:
				handler = HandlerTable.getInstance().get(ChatPetition.class);
				break;
			case SHOUT:
				handler = HandlerTable.getInstance().get(ChatShout.class);
				break;
			case TELL:
				handler = HandlerTable.getInstance().get(ChatTell.class);
				break;
			case TRADE:
				handler = HandlerTable.getInstance().get(ChatTrade.class);
				break;
			default:
				log.warn("Handler for {} not found!", _type);
				throw new UnsupportedOperationException();
		}

		if (handler != null) {
			handler.invoke(_type, activeChar, _target, _text);
		} else {
			_log.warn(activeChar.getName() + " tried to use unregistred chathandler type: " + _type + ".");
		}
	}

	private static boolean checkBot(String text) {
		for (String botCommand : WALKER_COMMAND_LIST) {
			if (text.startsWith(botCommand)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean triggersOnActionRequest() {
		return false;
	}
}
