package com.internship.managerview.util.enums;

/**
 * Enumeration of error codes and their corresponding HTTP status codes.
 * Note: The string values represent unique identifiers for each error condition.
 * The comments specify the associated HTTP status code for reference.
 *
 * @author Mays AlTimemy
 */
public enum ErrorCode {
    // General
    G_NUMERIC_ID_REQUIRED("G-1"), // 400 Bad Request
    G_UNAUTHORIZED("G-2"), // 401 Unauthorized
    G_SESSION_EXPIRED("G-3"), // 401 Unauthorized

    // Requests
    G_INVALID_REQUEST_BODY("G-4"), // 400 Bad Request
    G_INVALID_QUERY_CHARACTER("G-5"), // 400 Bad Request

    // Generic error
    G_GENERIC_ERROR("G-100"),

    // User
    U_NOT_FOUND("U-1"), // 404 Not Found
    U_EMAIL_ALREADY_EXISTS("U-2"), // 409 Conflict

    U_INVALID_CREDENTIALS("U-101"), // 401 Unauthorized

    // User creation/validation
    U_INVALID_LENGTH_FIRST_NAME("U-201"), // 400 Bad Request
    U_INVALID_LENGTH_LAST_NAME("U-202"), // 400 Bad Request
    U_INVALID_LENGTH_PREFIX("U-203"), // 400 Bad Request
    U_INVALID_EMAIL("U-204"), // 400 Bad Request
    U_EMAIL_IS_EMPTY("U-205"), // 400 Bad Request
    U_INVALID_PASSWORD("U-206"), // 400 Bad Request - validation of password
    U_INCORRECT_PASSWORD("U-207"), // 401 Unauthorized - old password doesn't match the password stored in the database
    U_NEW_PASSWORD_EQUAL_TO_OLD("U-208"), // 400 Bad Request
    U_INVALID_ROLE("U-209"), // 400 Bad Request
    U_INVALID_SECURITY_ANSWER("U-210"),

    // Teams
    T_NOT_FOUND("T-1"), // 404 Not Found
    T_NAME_ALREADY_EXISTS("T-2"), // 409 Conflict

    // Team name validation
    T_INVALID_NAME_LENGTH("T-101"), // 400 Bad Request
    T_INVALID_NAME_CHARACTER("T-102"), // 400 Bad Request
    T_INVALID_STARTING_CHARACTER("T-103"), // 400 Bad Request
    T_NAME_IS_EMPTY("T-104"), // 400 Bad Request

    // Team members
    T_MEMBER_NOT_FOUND("T-201"), // 404 Not Found
    T_INVALID_MEMBER_AMOUNT("T-202"), // 400 Bad Request
    T_DUPLICATE_MEMBER_IDS("T-203"), // 400 Bad Request
    T_MEMBER_IS_EMPTY("T-204"), // 400 Bad Request

    // Team avatar
    T_AVATAR_NOT_FOUND("T-301"), // 404 Not Found
    T_INVALID_MIME_TYPE("T-302"), // 400 Bad Request
    T_INVALID_SIZE("T-303"), // 400 Bad Request
    T_INVALID_EXTENSION("T-304"), // 400 Bad Request
    T_INVALID_DIMENSIONS("T-305"), // 400 Bad Request

    // Team creator - temporary error codes, can be removed once login is implemented and creator is derived from JWT.
    T_CREATOR_IS_EMPTY("T-10000"), // 400 Bad Request
    T_CREATOR_NOT_FOUND("T-10001"), // 404 Not Found

    // C4 model
    C1_NOT_FOUND("C1-1"), // 404 Not Found
    C2_NOT_FOUND("C2-1"), // 404 Not Found
    C3_NOT_FOUND("C3-1"); // 404 Not Found

    private final String code;

    private ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
