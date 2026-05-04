package com.sabianrobi.frameshelf.error;

import com.sabianrobi.frameshelf.error.exception.NotAuthorizedException;
import com.sabianrobi.frameshelf.error.exception.NotFoundException;
import com.sabianrobi.frameshelf.error.exception.ThirdPartyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${app.debug:false}")
    private boolean debugMode;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException ex) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    // Enum type cannot be parsed (e.g. 'Hungary' for Language enum (instead of Hungarian))
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageConversionException(final HttpMessageConversionException ex) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    // Thrown when a required query parameter is missing
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(final MissingServletRequestParameterException ex) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    // Jackson polymorphism error; like when request contains incorrect type
    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ErrorResponse> handleClassCastException(final ClassCastException ex) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(final NotAuthorizedException ex) {
        return getResponseEntity(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException ex) {
        return getResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ThirdPartyException.class)
    public ResponseEntity<ErrorResponse> handleThirdPartyException(final ThirdPartyException ex) {
        return getResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, ex);
    }

    // Fallback for any unhandled exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException ex) {
        final String[] funnyMessages = {
                "The server is having an existential crisis.",
                "Our code caught fire. Try again when the smoke clears.",
                "Something went kaboom. We're investigating the ashes.",
                "Our bad. The code gremlins are having a party right now.",
                "You broke something we didn't think was possible. Congrats."
        };

        final String randomMessage = funnyMessages[new java.util.Random().nextInt(funnyMessages.length)];

        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex, randomMessage);
    }

    // Helper methods
    private ResponseEntity<ErrorResponse> getResponseEntity(final HttpStatus httpStatus, final Throwable throwable) {
        return getResponse(httpStatus, throwable, throwable.getMessage());
    }

    private ResponseEntity<ErrorResponse> getResponseEntity(final HttpStatus httpStatus, final Throwable throwable, final String message) {
        return getResponse(httpStatus, throwable, message);
    }

    private ResponseEntity<ErrorResponse> getResponse(final HttpStatus httpStatus, final Throwable throwable, final String message) {
        return ResponseEntity
                .status(httpStatus.value())
                .body(new ErrorResponse(
                                httpStatus.value(),
                                httpStatus.getReasonPhrase(),
                                message,
                                getTraceIfDebug(throwable)
                        )
                );
    }

    private String getTraceIfDebug(final Throwable throwable) {
        return debugMode ? getStackTrace(throwable) : null;
    }

    private String getStackTrace(final Throwable throwable) {
        final StringBuilder sb = new StringBuilder();

        sb.append(throwable.toString());
        for (final StackTraceElement element : throwable.getStackTrace()) {
            sb.append(" at ").append(element.toString());
        }

        return sb.toString();
    }
}
