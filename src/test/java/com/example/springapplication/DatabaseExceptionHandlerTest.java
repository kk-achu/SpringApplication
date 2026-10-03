package com.example.springapplication;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseExceptionHandlerTest {

    @Test
    void returnsServiceUnavailableForDatabaseResourceFailure() {
        DatabaseExceptionHandler handler = new DatabaseExceptionHandler();

        ResponseEntity<ProblemDetail> response = handler.handleDatabaseUnavailable(
                new DataAccessResourceFailureException("Connection refused"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Database unavailable", response.getBody().getTitle());
        assertEquals(
                "The database is temporarily unavailable. Please try again later.",
                response.getBody().getDetail());
    }
}