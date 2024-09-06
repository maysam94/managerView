package com.internship.managerview.util.exceptionHandling;

import com.internship.managerview.util.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles general exceptions.
     *
     * @param exception The Exception object.
     * @param request   The WebRequest object.
     * @return A ResponseEntity containing an error response and HTTP status code.
     * @author Mays Altimemy
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.G_GENERIC_ERROR.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles the MaxUploadSizeExceededException that gets thrown at any point during the processing of a request.
     *
     * @return A ResponseEntity with status 400 and error code T_INVALID_SIZE (T-303)
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.T_INVALID_SIZE.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
