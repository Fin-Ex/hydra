package ru.finex.ws.l2.model;

import lombok.Getter;

/**
 * @author m0nster.mind
 */
public enum AuthFailReason {
    LOGIN_SUCCESS(0, true),
    NO_TEXT(0),
    SYSTEM_ERROR_LOGIN_LATER(1),
    PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT(2),
    PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT2(3),
    ACCESS_FAILED_TRY_LATER(4),
    INCORRECT_ACCOUNT_INFO_CONTACT_CUSTOMER_SUPPORT(5),
    ACCESS_FAILED_TRY_LATER2(6),
    ACOUNT_ALREADY_IN_USE(7),
    ACCESS_FAILED_TRY_LATER3(8),
    ACCESS_FAILED_TRY_LATER4(9),
    ACCESS_FAILED_TRY_LATER5(10);

    @Getter
    private final int messageId;
    @Getter
    private final boolean isSuccess;

    AuthFailReason(int messageId, boolean isSuccess) {
        this.messageId = messageId;
        this.isSuccess = isSuccess;
    }

    AuthFailReason(int messageId) {
        this(messageId, false);
    }

}
