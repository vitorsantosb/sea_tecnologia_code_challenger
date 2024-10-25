package org.code_challenger.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseBuilder {

    public static ResponseEntity<Map<String, Object>> CreateHttpResponse(
            String message,
            HttpStatus statusCode,
            String requestMessage,
            String method,
            String url,
            Object data) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> requestInfo = new HashMap<>();
        response.put("message", message);
        response.put("statusCode", statusCode.value());
        response.put("request", requestInfo);

        requestInfo.put("message", requestMessage);
        requestInfo.put("method", method);
        requestInfo.put("url", url);


        if (data != null) {
            response.put("data", data);
        }

        return new ResponseEntity<>(response, statusCode);
    }
}

