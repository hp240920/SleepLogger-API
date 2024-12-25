package com.noom.interview.fullstack.sleep.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class UpdateTimeRequest(
        val username: String,
        val date: LocalDate,
        val timeInBedStart: LocalDateTime,
        val timeInBedEnd: LocalDateTime,
        val totalTimeInBed: Int
)