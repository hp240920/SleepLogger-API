package com.noom.interview.fullstack.sleep.dto

import com.noom.interview.fullstack.sleep.entity.SleepLog
import java.time.LocalDate
import java.time.LocalTime

data class Last30DayAveragesResponse(
        val startDate: LocalDate,
        val endDate: LocalDate,
        val averageTotalTimeInBed: Double,
        val averageTimeInBedStart: LocalTime,
        val averageTimeInBedEnd: LocalTime,
        val feelingFrequencies: Map<SleepLog.Feeling, Int>,
        val daysIncluded: Int
)