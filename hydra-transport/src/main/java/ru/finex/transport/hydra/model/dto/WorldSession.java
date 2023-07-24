package ru.finex.transport.hydra.model.dto;

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
