package com.noom.interview.fullstack.sleep.dto

import com.noom.interview.fullstack.sleep.entity.SleepLog
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class SleepLogResponse(
        val id: UUID,
        val username: String, // Use username instead of userId
        val date: LocalDate,
        val timeInBedStart: LocalDateTime,
        val timeInBedEnd: LocalDateTime,
        val totalTimeInBed: Int,
        val feeling: String
) {
    companion object {
        fun fromEntity(sleepLog: SleepLog): SleepLogResponse {
            return SleepLogResponse(
                    id = sleepLog.id!!,
                    username = sleepLog.username,
                    date = sleepLog.date,
                    timeInBedStart = sleepLog.timeInBedStart,
                    timeInBedEnd = sleepLog.timeInBedEnd,
                    totalTimeInBed = sleepLog.totalTimeInBed,
                    feeling = sleepLog.feeling.name
            )
        }
    }
}
