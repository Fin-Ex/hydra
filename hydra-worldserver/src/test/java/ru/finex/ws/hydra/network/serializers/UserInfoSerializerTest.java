package ru.finex.ws.hydra.network.serializers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import ru.finex.ws.hydra.network.SerializerHelper;
import ru.finex.ws.hydra.network.model.UserInfoComponent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

/**
 * @author m0nster.mind
 */
public class UserInfoSerializerTest {

    @RepeatedTest(10)
    public void compareFlagsWithUnity() {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int count = 4 + rng.nextInt(UserInfoComponent.count() - 4);
        Set<UserInfoComponent> components = new HashSet<>();
        for (int i = 0; i < count; i++) {
            components.add(UserInfoComponent.values()[rng.nextInt(UserInfoComponent.count())]);
        }

        int finexFlags = buildFinexFlags(components);
        int unityFlags = buildUnityFlags(components);
        Assertions.assertEquals(unityFlags, finexFlags);
    }

    private int buildFinexFlags(Collection<UserInfoComponent> components) {
        int flags = components.stream()
            .mapToInt(e -> 1 << e.ordinal())
            .reduce(0, (e1, e2) -> e1 | e2);

        int reversed = SerializerHelper.reverse((byte) flags) & 0xff;
        reversed |= (SerializerHelper.reverse((byte) (flags >>> 8)) & 0xff) << 8;
        reversed |= (SerializerHelper.reverse((byte) (flags >>> 16)) & 0xff) << 16;

        if ((reversed & 0x800000) != 0) {
            reversed |= 0xff000000;
        }

        return reversed;
    }

    private int buildUnityFlags(Collection<UserInfoComponent> components) {
        // there logic from unity
        // https://bitbucket.org/UnAfraid/l2junity/

        byte[] DEFAULT_FLAG_ARRAY = {
                (byte) 0x80,
                0x40,
                0x20,
                0x10,
                0x08,
                0x04,
                0x02,
                0x01
        };

        BiConsumer<byte[], Integer> addMask = (masks, flag) -> masks[flag >> 3] |= DEFAULT_FLAG_ARRAY[flag & 7];

        byte[] masks = new byte[3];
        components.forEach(e -> addMask.accept(masks, e.ordinal()));

        // build a signed int32_t
        int value = masks[0] & 0xff |
            (masks[1] & 0xff) << 8 |
            (masks[2] & 0xff) << 16;

        if ((value & 0x800000) != 0) {
            value |= 0xff000000;
        }

        return value;
    }

}
