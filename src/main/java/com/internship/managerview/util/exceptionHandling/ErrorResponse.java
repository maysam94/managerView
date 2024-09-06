package com.internship.managerview.util.exceptionHandling;

public class ErrorResponse {
    private String errorCode;

    /**
     * Creates an errorCode instance.
     *
     * @param errorCode The integer associated with the error
     * @author Anne Butter
     */
    public ErrorResponse(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets the error code.
     *
     * @return The error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
