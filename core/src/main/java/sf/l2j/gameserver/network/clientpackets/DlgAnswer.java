package sf.l2j.gameserver.network.clientpackets;

import sf.finex.handlers.IDialogAnswer;
import sf.finex.handlers.dialog.DlgManager;
import sf.l2j.gameserver.model.actor.Player;

/**
 * @author Dezmond_snz Format: cddd
 */
public final class DlgAnswer extends L2GameClientPacket {

	private int msgId;
	private int answer;
	private int requesterId;

	@Override
	protected void readImpl() {
		msgId = readD();
		answer = readD();
		requesterId = readD();
	}

	@Override
	public void runImpl() {
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null) {
			return;
		}

		final IDialogAnswer dlgAnswer = DlgManager.getInstance().getAnswer(msgId);
		if (dlgAnswer != null) {
			dlgAnswer.handle(activeChar, answer, requesterId);
		} else {
			_log.warn("Handler for dialog ID = " + msgId + " not found!");
		}
	}
}
