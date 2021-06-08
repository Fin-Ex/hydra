package sf.finex.service;

import com.google.inject.ImplementedBy;
import sf.finex.id.RuntimeIdServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(RuntimeIdServiceImpl.class)
public interface RuntimeIdService {

    int generateId();

}
