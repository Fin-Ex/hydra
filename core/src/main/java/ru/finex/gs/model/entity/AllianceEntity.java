package ru.finex.gs.model.entity;

import java.util.List;

/**
 * @author m0nster.mind
 */
public class AllianceEntity {

    private int persistenceId;

    private int crestId;
    private byte[] crest;

    private List<ClanEntity> clans;

}
