package com.internship.managerview.testHelpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.managerview.util.enums.ErrorCode;
import com.internship.managerview.util.exceptionHandling.ErrorResponse;

public class GeneralTestHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Gets the ErrorResponse JSON object as a string with the given error code.
     *
     * @param errorCode The code that the response should have
     * @return A string containing the necessary JSON object
     * @throws JsonProcessingException The exception potentially thrown by objectMapper.writeValueAsString()
     */
    public static String getErrorResponse(ErrorCode errorCode) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode());
        return objectMapper.writeValueAsString(errorResponse);
    }
}
