package com.internship.managerview.util.exceptionHandling.exceptions;

import com.internship.managerview.util.enums.ErrorCode;

public class InvalidInputException extends RuntimeException {
    ErrorCode errorCode;

    /**
     * Creates a DataRowNotFoundException instance.
     *
     * @param errorCode The code representing the error details
     */
    public InvalidInputException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
