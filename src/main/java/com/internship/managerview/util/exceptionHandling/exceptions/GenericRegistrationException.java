package com.internship.managerview.util.exceptionHandling.exceptions;

import com.internship.managerview.util.enums.ErrorCode;

/**
 * A generic exception class used for registration-related errors.
 * This exception is thrown when an error occurs during the registration process
 * that cannot be categorized by more specific exception types.
 * It encapsulates an {@link ErrorCode} that helps identify the specific error condition.
 *
 * @author: Mays AlTimemy
 */
public class GenericRegistrationException extends RuntimeException {
    private final ErrorCode errorCode;

    /**
     * Constructs a new GenericRegistrationException with the specified ErrorCode.
     * The ErrorCode helps in identifying the specific reason for the exception.
     *
     * @param errorCode The ErrorCode associated with this exception.
     *@author: Mays AlTimemy
     */
    public GenericRegistrationException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    /**
     * Retrieves the ErrorCode associated with this exception.
     *
     * @return The ErrorCode that identifies the specific error condition.
     * @author: Mays AlTimemy
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
