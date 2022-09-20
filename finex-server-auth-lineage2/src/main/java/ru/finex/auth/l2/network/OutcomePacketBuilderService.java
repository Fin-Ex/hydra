package ru.finex.auth.l2.network;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.model.AccountKickedReason;
import ru.finex.auth.l2.model.LoginFailReason;
import ru.finex.auth.l2.network.model.dto.AccountKickedDto;
import ru.finex.auth.l2.network.model.dto.GGAuthDto;
import ru.finex.auth.l2.network.model.dto.InitDto;
import ru.finex.auth.l2.network.model.dto.LoginFailDto;
import ru.finex.auth.l2.network.model.dto.LoginOkDto;
import ru.finex.network.netty.model.NetworkDto;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class OutcomePacketBuilderService {

    private final AuthCodecService authCodecService;

    public NetworkDto init(GameSession session) {
        return InitDto.builder()
            .sessionId(session.getSessionId())
            .publicKey(authCodecService.getPublicKey())
            .blowfishKey(authCodecService.getBlowfishKey())
            .protocolRevision(0x0000c621)
            .build();
    }

    public NetworkDto loginFail(LoginFailReason reason) {
        return new LoginFailDto(reason.getMessageId());
    }

    /**
     * Account banned by specified reason.
     * @param reason reason
     * @return packet
     */
    public NetworkDto accountKicked(AccountKickedReason reason) {
        return new AccountKickedDto(reason.getMessageId());
    }

    public NetworkDto loginOk(GameSession session) {
        return new LoginOkDto(session.getSessionKey());
    }

    public NetworkDto ggAuth(GameSession session) {
        return GGAuthDto.builder()
            .sessionId(session.getSessionId())
            .unk1(0x00)
            .unk2(0x00)
            .unk3(0x00)
            .unk4(0x00)
            .build();
    }

}
