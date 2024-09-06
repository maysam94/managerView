package com.internship.managerview.util.exceptionHandling.exceptions;

import com.internship.managerview.util.enums.ErrorCode;

public class EntityAlreadyExistsException extends RuntimeException {
    ErrorCode errorCode;

    /**
     * Creates a DataRowNotFoundException instance.
     *
     * @param errorCode The code representing the error details
     */
    public EntityAlreadyExistsException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
