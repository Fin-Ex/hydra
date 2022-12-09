package ru.finex.auth.l2.network.serial;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import ru.finex.auth.l2.network.model.dto.RequestAuthLoginDto;
import ru.finex.auth.l2.service.AuthCodecService;
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
public class RequestOldAuthLoginDeserializer implements PacketDeserializer<RequestAuthLoginDto> {

    private final AuthCodecService authCodecService;

    @Override
    public RequestAuthLoginDto deserialize(ByteBuf buffer) {
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
            .sessionId(buffer.readIntLE())
            .unk1(buffer.readIntLE())
            .unk2(buffer.readIntLE())
            .unk3(buffer.readIntLE())
            .unk4(buffer.readIntLE())
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
        if (length >= 128) {
            bytes = new byte[128];
        } else {
            throw new RuntimeException("Unknown payload, length: " + length);
        }

        buffer.readBytes(bytes);
        return bytes;
    }

}
