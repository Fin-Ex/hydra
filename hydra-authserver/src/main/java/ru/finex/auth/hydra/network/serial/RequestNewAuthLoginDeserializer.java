package ru.finex.auth.hydra.network.serial;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import ru.finex.auth.hydra.network.model.dto.RequestAuthLoginDto;
import ru.finex.auth.hydra.service.AuthCodecService;
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
public class RequestNewAuthLoginDeserializer implements PacketDeserializer<RequestAuthLoginDto> {

    private final AuthCodecService authCodecService;

    @Override
    public RequestAuthLoginDto deserialize(ByteBuf buffer) {
        byte[] payload;
        try {
            payload = readAndDecrypt(buffer);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        String login = payloadToLogin(payload).toLowerCase();
        String password = new String(payload, 0xdc, 16).trim();
        int ncotp = payload[0xfc] & 0xff;
        ncotp |= (payload[0xfd] & 0xff) << 8;
        ncotp |= (payload[0xfe] & 0xff) << 16;
        ncotp |= (payload[0xff] & 0xff) << 24;

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

    private String payloadToLogin(byte[] payload) {
        return new String(payload, 0x4e, 50).trim()
            + new String(payload, 0xce, 14).trim();
    }

    private byte[] readAndDecrypt(ByteBuf buffer) throws GeneralSecurityException {
        byte[] encrypted = read(buffer);

        Cipher cipher = Cipher.getInstance("RSA/ECB/nopadding");
        cipher.init(Cipher.DECRYPT_MODE, authCodecService.getPrivateKey());

        byte[] decrypted = new byte[256];
        for (int i = 0; i < encrypted.length; i += 128) {
            cipher.doFinal(encrypted, i, 128, decrypted, i);
        }

        return decrypted;
    }

    private byte[] read(ByteBuf buffer) {
        byte[] bytes;
        int length = buffer.readableBytes();
        if (length >= 256) {
            bytes = new byte[256];
        } else {
            throw new RuntimeException("Unknown payload, length: " + length);
        }

        buffer.readBytes(bytes);
        return bytes;
    }

}
