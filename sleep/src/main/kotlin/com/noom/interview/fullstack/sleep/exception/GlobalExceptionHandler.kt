package com.noom.interview.fullstack.sleep.exception

import com.noom.interview.fullstack.sleep.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.converter.HttpMessageNotReadableException
import java.time.format.DateTimeParseException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(ex: DateTimeParseException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
                message = "Invalid date or time format",
                error = ex.message ?: "Date/Time parsing failed",
                status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
                message = "Invalid argument",
                error = ex.message ?: "Argument parsing failed",
                status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
                message = "Resource not found",
                error = ex.message ?: "Resource not found",
                status = HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
                message = "Invalid request body format",
                error = ex.message ?: "Request body parsing failed",
                status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
                message = "An unexpected error occurred",
                error = ex.message ?: "Unknown error",
                status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
