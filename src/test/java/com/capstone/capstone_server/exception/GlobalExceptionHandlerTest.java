package com.capstone.capstone_server.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @Test
    //BadRequest를 리턴해야하는 IllegalArgumentException 테스트
    void handleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException();

        ResponseEntity<Object> response = new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(exception,response.getBody());

    }
}