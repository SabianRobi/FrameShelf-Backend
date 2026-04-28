package com.sabianrobi.frameshelf.error;

import com.sabianrobi.frameshelf.error.exception.NotAuthorizedException;
import com.sabianrobi.frameshelf.error.exception.ThirdPartyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException ex) {
        final ErrorResponse error = new ErrorResponse(
                400,
                "Bad Request",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(final NotAuthorizedException ex) {

        final ErrorResponse error = new ErrorResponse(
                403,
                "Not Authorized",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(final NoResourceFoundException exception) {
        final ErrorResponse error = new ErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ThirdPartyException.class)
    public ResponseEntity<ErrorResponse> handleThirdPartyException(final ThirdPartyException ex) {
        final ErrorResponse error = new ErrorResponse(
                503,
                "Service Unavailable",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}