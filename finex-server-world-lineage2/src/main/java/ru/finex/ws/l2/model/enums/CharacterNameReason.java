package ru.finex.ws.l2.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author m0nster.mind
 */
@Getter
@RequiredArgsConstructor
public enum CharacterNameReason implements IdEnum {
    OK(-1),
    CHARACTER_CREATE_FAILED(1),
    NAME_ALREADY_EXISTS(2),
    INVALID_LENGTH(3),
    INVALID_NAME(4),
    CANNOT_CREATE_SERVER(5);

    private final int id;

}
