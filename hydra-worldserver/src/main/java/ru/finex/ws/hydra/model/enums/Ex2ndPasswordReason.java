package ru.finex.ws.hydra.model.enums;

import lombok.Getter;

/**
 * @author m0nster.mind
 */
@Getter
public enum Ex2ndPasswordReason implements IdEnum {

    SETUP,
    PROMPT,
    OK;

    @Override
    public int getId() {
        return ordinal();
    }
}
