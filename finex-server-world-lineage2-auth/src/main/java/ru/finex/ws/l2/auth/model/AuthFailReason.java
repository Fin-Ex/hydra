package ru.finex.ws.l2.auth.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public enum AuthFailReason {
    NO_TEXT(true),
    SYSTEM_ERROR_LOGIN_LATER,
    PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT,
    PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT2,
    ACCESS_FAILED_TRY_LATER,
    INCORRECT_ACCOUNT_INFO_CONTACT_CUSTOMER_SUPPORT,
    ACCESS_FAILED_TRY_LATER2,
    ACOUNT_ALREADY_IN_USE,
    ACCESS_FAILED_TRY_LATER3,
    ACCESS_FAILED_TRY_LATER4,
    ACCESS_FAILED_TRY_LATER5;

    @Getter private final boolean isSuccess;

    private AuthFailReason() {
        isSuccess = false;
    }
}
