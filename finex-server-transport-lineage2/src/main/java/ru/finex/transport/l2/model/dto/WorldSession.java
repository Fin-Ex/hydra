package ru.finex.transport.l2.model.dto;

import lombok.Data;

/**
 * @author m0nster.mind
 */
@Data
public class WorldSession {

    private long userId;
    private int sessionId;
    private long sessionKey;

}
