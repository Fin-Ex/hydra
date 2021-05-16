/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.handlers.dialog;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import sf.finex.handlers.IDialogAnswer;
import sf.finex.handlers.IDialogRequest;
import sf.finex.handlers.dialog.answers.EngageAnswer;
import sf.finex.handlers.dialog.answers.GateAnswer;
import sf.finex.handlers.dialog.answers.ReviveAnswer;
import sf.finex.handlers.dialog.answers.TalentLearnAnswer;
import sf.finex.handlers.dialog.answers.TalentResetAnswer;
import sf.finex.handlers.dialog.answers.TeleportAnswer;
import sf.finex.handlers.dialog.requests.ReviveRequest;
import sf.finex.handlers.dialog.requests.TalentLearnRequest;
import sf.finex.handlers.dialog.requests.TalentResetRequest;
import sf.finex.handlers.dialog.requests.TeleportRequest;
import sf.l2j.gameserver.network.SystemMessageId;

/**
 *
 * @author FinFan
 */
public class DlgManager {

	@Getter
	private static final DlgManager instance = new DlgManager();

	private final Map<Integer, IDialogAnswer> answers = new HashMap<>();
	private final Map<Class<?>, IDialogRequest> requests = new HashMap<>();

	public DlgManager() {
		// answers
		final ReviveAnswer reviveRequest = new ReviveAnswer();
		answers.put(SystemMessageId.RESSURECTION_REQUEST_BY_S1.getId(), reviveRequest);
		answers.put(SystemMessageId.DO_YOU_WANT_TO_BE_RESTORED.getId(), reviveRequest);
		answers.put(SystemMessageId.S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT.getId(), new TeleportAnswer());
		answers.put(1983, new EngageAnswer());
		answers.put(SystemMessageId.WOULD_YOU_LIKE_TO_OPEN_THE_GATE.getId(), new GateAnswer(true));
		answers.put(SystemMessageId.WOULD_YOU_LIKE_TO_CLOSE_THE_GATE.getId(), new GateAnswer(false));
		answers.put(SystemMessageId.DO_YOU_REALY_WANT_TO_LEARN_S1_TALENT.getId(), new TalentLearnAnswer());
		answers.put(SystemMessageId.DO_YOU_WANT_TO_RESET_ALL_YOUR_MASTERIES_FOR_S1_S2.getId(), new TalentResetAnswer()); // reset with pay
		answers.put(SystemMessageId.DO_YOU_WANT_TO_RESET_ALL_YOUR_MASTERIES.getId(), new TalentResetAnswer()); // free reset

		// requests
		requests.put(TeleportRequest.class, new TeleportRequest());
		requests.put(ReviveRequest.class, new ReviveRequest());
		requests.put(TalentLearnRequest.class, new TalentLearnRequest());
		requests.put(TalentResetRequest.class, new TalentResetRequest());
	}

	public IDialogAnswer getAnswer(int msgId) {
		return answers.get(msgId);
	}

	public <T> T getRequest(Class<T> type) {
		return (T) requests.get(type);
	}
}
