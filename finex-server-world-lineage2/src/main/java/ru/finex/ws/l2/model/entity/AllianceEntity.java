package ru.finex.ws.l2.model.entity;

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
public class AllianceEntity {

    private int persistenceId;

    private int crestId;
    private byte[] crest;

    private List<ClanEntity> clans;

}
