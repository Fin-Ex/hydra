package ru.finex.ws.l2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author m0nster.mind
 */
public class LocalContext {

    private static final ThreadLocal<Map<String, Object>> CTX = ThreadLocal.withInitial(HashMap::new);

    public static Map<String, Object> get() {
        return CTX.get();
    }

}
