package com.Proton.JavaSpring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception e, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(webRequest.getDescription(false).replace("uri=", ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        String message = getString(e);

        errorResponse.setMessage(message != null ? message : "An error occurred");
        return errorResponse;
    }

    private static String getString(Exception e) {
        String message = e.getMessage();
        if (message != null && message.contains("[") && message.contains("]")) {
            int start = message.lastIndexOf("[");
            int end = message.lastIndexOf("]");
            if (start != -1 && end != -1 && start < end) {
                try {
                    message = message.substring(start + 1, end);
                } catch (StringIndexOutOfBoundsException ex) {
                    // Keep original message if extraction fails
                }
            }
        }
        return message;
    }
}
