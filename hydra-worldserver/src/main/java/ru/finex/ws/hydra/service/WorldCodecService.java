package ru.finex.ws.hydra.service;

import lombok.Getter;
import ru.finex.core.rng.RandomProviders;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class WorldCodecService {

    private static final int KEY_SIZE = 16;
    private static final byte[] STATIC_PART = new byte[] {
        (byte) 0xc8,
        (byte) 0x27,
        (byte) 0x93,
        (byte) 0x01,
        (byte) 0xa1,
        (byte) 0x6c,
        (byte) 0x31,
        (byte) 0x97
    };

    @Getter
    private byte[] blowfishKey;
    @Getter
    private byte[] emptyKey;

    public WorldCodecService() {
        byte[] generated = new byte[KEY_SIZE - STATIC_PART.length];
        RandomProviders.secureRandom().get()
            .nextBytes(generated);
        blowfishKey = buildKey(generated);
        emptyKey = buildKey(new byte[KEY_SIZE - STATIC_PART.length]);
    }

    private byte[] buildKey(byte[] generated) {
        byte[] key = new byte[KEY_SIZE];
        System.arraycopy(generated, 0, key, 0, generated.length);
        System.arraycopy(STATIC_PART, 0, key, generated.length, STATIC_PART.length);
        return key;
    }

}
