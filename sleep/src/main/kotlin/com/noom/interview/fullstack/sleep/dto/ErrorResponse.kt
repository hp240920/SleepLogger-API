package com.noom.interview.fullstack.sleep.dto

data class ErrorResponse(
        val message: String,
        val error: String,
        val status: Int
)
