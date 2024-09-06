package com.internship.managerview.util.exceptionHandling.exceptions;

import com.internship.managerview.util.enums.ErrorCode;

public class DataRowNotFoundException extends RuntimeException {
    ErrorCode errorCode;

    /**
     * Creates a DataRowNotFoundException instance.
     *
     * @param errorCode The code representing the error details
     */
    public DataRowNotFoundException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
