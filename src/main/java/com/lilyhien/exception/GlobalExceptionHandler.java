package com.lilyhien.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

//Because your ApiErrorDto requires a path, and the only way to get the URL path of the failed request is to ask Spring to give it to me.
//HttpServletRequest is a Java interface that provides a structured view of an incoming HTTP request.
//Servlet = Java’s low-level way to handle HTTP requests on a server
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest req) {
        return buildResponse(e, req, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorDto> handleUnauthorized(UnauthorizedException e, HttpServletRequest req) {
        return buildResponse(e, req, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorDto> handleValidation(ValidationException e, HttpServletRequest req) {
        return buildResponse(e, req, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGlobalException(Exception e, HttpServletRequest req) {
        // from lombok, works with SLF4J
        log.error("Unexpected error occurred at {}: ", req.getRequestURI(), e);

        // Security: Return generic message for unknown errors
        String secureMessage = "An internal server error occurred.";
        ApiErrorDto payload = new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, secureMessage, req.getRequestURI());
        return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest req) {

        // extracts the specific error messages wrote in the DTO (like "Email is required")
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        // pass a new exception with our message buildResponse helper
        return buildResponse(new Exception(errorMessage), req, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiErrorDto> buildResponse(Exception e, HttpServletRequest request, HttpStatus status) {
        String path = request.getRequestURI();
        ApiErrorDto payload = new ApiErrorDto(status, e.getMessage(), path);
        return new ResponseEntity<>(payload, status);
    }
}