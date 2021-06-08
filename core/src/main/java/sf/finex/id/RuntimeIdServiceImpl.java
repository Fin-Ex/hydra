package sf.finex.id;

import sf.finex.service.RuntimeIdService;

import java.util.BitSet;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class RuntimeIdServiceImpl implements RuntimeIdService {

    private final BitSet bitSet = new BitSet();
    private int position;

    @Override
    public synchronized int generateId() {
        int id = position;

        bitSet.set(position);
        int nextPosition = bitSet.nextClearBit(position);
        position = nextPosition;

        return id;
    }

}
