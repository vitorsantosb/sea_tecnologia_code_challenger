package org.code_challenger.Exception;

import org.code_challenger.services.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseBuilder.CreateHttpResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                "One or more emails are invalid",
                "POST",
                "/user/create",
                ex.getCause()
        );
    }
}
