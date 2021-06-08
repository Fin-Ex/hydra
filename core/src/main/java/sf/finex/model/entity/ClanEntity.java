package sf.finex.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author m0nster.mind
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClanEntity implements Entity {

    private int persistenceId;

    private int crestId;
    private byte[] crest;

    private int largeCrestId;
    private byte[] largeCrest;

    private List<PlayerEntity> members;

}
