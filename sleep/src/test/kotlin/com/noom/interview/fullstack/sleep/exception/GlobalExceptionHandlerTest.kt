package com.noom.interview.fullstack.sleep.exception

import com.noom.interview.fullstack.sleep.dto.ErrorResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import java.time.format.DateTimeParseException

class GlobalExceptionHandlerTest {

    private lateinit var globalExceptionHandler: GlobalExceptionHandler

    @BeforeEach
    fun setUp() {
        globalExceptionHandler = GlobalExceptionHandler()
    }

    @Test
    fun `should handle DateTimeParseException`() {
        val exception = DateTimeParseException("Invalid date format", "2024-12-24", 0)

        val response: ResponseEntity<ErrorResponse> = globalExceptionHandler.handleDateTimeParseException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Invalid date or time format", response.body?.message)
        assertEquals("Invalid date format", response.body?.error)
    }

    @Test
    fun `should handle IllegalArgumentException`() {
        val exception = IllegalArgumentException("Invalid argument provided")

        val response: ResponseEntity<ErrorResponse> = globalExceptionHandler.handleIllegalArgumentException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Invalid argument", response.body?.message)
        assertEquals("Invalid argument provided", response.body?.error)
    }

    @Test
    fun `should handle NoSuchElementException`() {
        val exception = NoSuchElementException("Resource not found")

        val response: ResponseEntity<ErrorResponse> = globalExceptionHandler.handleNoSuchElementException(exception)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Resource not found", response.body?.message)
        assertEquals("Resource not found", response.body?.error)
    }

    @Test
    fun `should handle HttpMessageNotReadableException`() {
        val exception = HttpMessageNotReadableException("Malformed JSON request")

        val response: ResponseEntity<ErrorResponse> = globalExceptionHandler.handleHttpMessageNotReadableException(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Invalid request body format", response.body?.message)
        assertEquals("Malformed JSON request", response.body?.error)
    }

    @Test
    fun `should handle General Exception`() {
        val exception = Exception("Unexpected error occurred")

        val response: ResponseEntity<ErrorResponse> = globalExceptionHandler.handleGeneralException(exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertNotNull(response.body)
        assertEquals("An unexpected error occurred", response.body?.message)
        assertEquals("Unexpected error occurred", response.body?.error)
    }
}
