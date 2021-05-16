package sf.l2j.gameserver.communitybbs.Manager;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

import sf.l2j.gameserver.cache.HtmCache;
import sf.l2j.gameserver.model.actor.Player;
import sf.l2j.gameserver.network.serverpackets.ShowBoard;

public abstract class BaseBBSManager {

	protected static final Logger _log = LoggerFactory.getLogger(BaseBBSManager.class.getName());

	protected static final String CB_PATH = "data/html/CommunityBoard/";

	public void parseCmd(String command, Player activeChar) {
		separateAndSend("<html><body><br><br><center>The command: " + command + " isn't implemented.</center></body></html>", activeChar);
	}

	public void parseWrite(String ar1, String ar2, String ar3, String ar4, String ar5, Player activeChar) {
		separateAndSend("<html><body><br><br><center>The command: " + ar1 + " isn't implemented.</center></body></html>", activeChar);
	}

	public static void separateAndSend(String html, Player acha) {
		if (html == null || acha == null) {
			return;
		}

		if (html.length() < 4090) {
			acha.sendPacket(new ShowBoard(html, "101"));
			acha.sendPacket(ShowBoard.STATIC_SHOWBOARD_102);
			acha.sendPacket(ShowBoard.STATIC_SHOWBOARD_103);
		} else if (html.length() < 8180) {
			acha.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			acha.sendPacket(new ShowBoard(html.substring(4090, html.length()), "102"));
			acha.sendPacket(ShowBoard.STATIC_SHOWBOARD_103);
		} else if (html.length() < 12270) {
			acha.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			acha.sendPacket(new ShowBoard(html.substring(4090, 8180), "102"));
			acha.sendPacket(new ShowBoard(html.substring(8180, html.length()), "103"));
		}
	}

	protected static void send1001(String html, Player acha) {
		if (html.length() < 8180) {
			acha.sendPacket(new ShowBoard(html, "1001"));
		}
	}

	protected static void send1002(Player acha) {
		send1002(acha, " ", " ", "0");
	}

	protected static void send1002(Player activeChar, String string, String string2, String string3) {
		List<String> _arg = new ArrayList<>();
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add(activeChar.getName());
		_arg.add(Integer.toString(activeChar.getObjectId()));
		_arg.add(activeChar.getAccountName());
		_arg.add("9");
		_arg.add(string2);
		_arg.add(string2);
		_arg.add(string);
		_arg.add(string3);
		_arg.add(string3);
		_arg.add("0");
		_arg.add("0");
		activeChar.sendPacket(new ShowBoard(_arg));
	}

	/**
	 * Loads an HTM located in the default CB path.
	 *
	 * @param file : the file to load.
	 * @param activeChar : the requester.
	 */
	protected void loadStaticHtm(String file, Player activeChar) {
		separateAndSend(HtmCache.getInstance().getHtm(CB_PATH + getFolder() + file), activeChar);
	}

	/**
	 * That method is overidden in every board type. It allows to switch of
	 * folders following the board.
	 *
	 * @return the folder.
	 */
	protected String getFolder() {
		return "";
	}
}
