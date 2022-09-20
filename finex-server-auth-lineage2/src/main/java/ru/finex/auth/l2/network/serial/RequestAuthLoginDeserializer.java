package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.command.network.RequestAuthLoginCommand;
import ru.finex.auth.l2.network.AuthCodecService;
import ru.finex.auth.l2.network.model.dto.RequestAuthLoginDto;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;

import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
@IncomePacket(value = @Opcode(0x00), command = @Cmd(RequestAuthLoginCommand.class))
public class RequestAuthLoginDeserializer implements PacketDeserializer<RequestAuthLoginDto> {

    private final AuthCodecService authCodecService;

    @Override
    public RequestAuthLoginDto deserialize(ByteBuf buffer) {
        if (isNewAuthStruct(buffer)) {
            throw new RuntimeException("not implemented");
        }

        byte[] payload;
        try {
            payload = readAndDecrypt(buffer);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        String login = new String(payload, 0x5e, 14).trim().toLowerCase();
        String password = new String(payload, 0x6c, 16).trim();
        int ncotp = payload[0x7c] & 0xff;
        ncotp |= (payload[0x7d] & 0xff) << 8;
        ncotp |= (payload[0x7e] & 0xff) << 16;
        ncotp |= (payload[0x7f] & 0xff) << 24;

        return RequestAuthLoginDto.builder()
            .login(login)
            .password(password)
            .ncOtp(ncotp)
            .build();
    }

    private byte[] readAndDecrypt(ByteBuf buffer) throws GeneralSecurityException {
        byte[] encrypted = read(buffer);

        Cipher cipher = Cipher.getInstance("RSA/ECB/nopadding");
        cipher.init(Cipher.DECRYPT_MODE, authCodecService.getPrivateKey());
        return cipher.doFinal(encrypted);
    }

    private byte[] read(ByteBuf buffer) {
        byte[] bytes;
        int length = buffer.readableBytes();
        if (length >= 256) {
            bytes = new byte[256];
        } else if (length >= 128) {
            bytes = new byte[128];
        } else {
            throw new RuntimeException("Unknown payload, length: " + length);
        }

        buffer.readBytes(bytes);
        return bytes;
    }

    private boolean isNewAuthStruct(ByteBuf buffer) {
        return buffer.readableBytes() >= 256;
    }

}
