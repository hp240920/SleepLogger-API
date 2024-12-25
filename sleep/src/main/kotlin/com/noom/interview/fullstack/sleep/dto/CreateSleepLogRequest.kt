package com.noom.interview.fullstack.sleep.dto

import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class CreateSleepLogRequest(
        @field:NotNull(message = "Username is required")
        val username: String,

        val date: LocalDate = LocalDate.now(),

        @field:NotNull(message = "Start time is required")
        val timeInBedStart: LocalDateTime,

        @field:NotNull(message = "End time is required")
        val timeInBedEnd: LocalDateTime,

        @field:Min(value = 0, message = "Total time in bed must be positive")
        val totalTimeInBed: Int,

        val feeling: String
)
