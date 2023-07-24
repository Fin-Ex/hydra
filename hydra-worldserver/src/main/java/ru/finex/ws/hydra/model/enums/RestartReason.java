package ru.finex.ws.hydra.model.enums;

import lombok.Getter;

/**
 * @author m0nster.mind
 */
@Getter
public enum RestartReason implements IdEnum {

    NOT_ALLOWED,
    OK;

    @Override
    public int getId() {
        return ordinal();
    }

}
