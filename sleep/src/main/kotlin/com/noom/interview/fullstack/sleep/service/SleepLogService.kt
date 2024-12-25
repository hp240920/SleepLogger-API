package com.noom.interview.fullstack.sleep.service

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequest
import com.noom.interview.fullstack.sleep.dto.Last30DayAveragesResponse
import com.noom.interview.fullstack.sleep.dto.SleepLogResponse
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository
import com.noom.interview.fullstack.sleep.entity.SleepLog
import com.noom.interview.fullstack.sleep.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class SleepLogService @Autowired constructor(
        private val sleepLogRepository: SleepLogRepository,
        private val userRepository: UserRepository
) {

    // Helper function to check if user exists
    private fun checkUserExists(username: String) {
        val userExists = userRepository.existsByUsername(username)
        if (!userExists) {
            throw IllegalArgumentException("User with username $username does not exist")
        }
    }

    fun createSleepLog(request: CreateSleepLogRequest): SleepLog {
        // Check if the user exists
        checkUserExists(request.username)

        // Check if a sleep log already exists for the user on the same date
        val existingLog = sleepLogRepository.findByUsernameAndDate(request.username, request.date)
        if (existingLog != null) {
            throw IllegalArgumentException("Sleep log for user ${request.username} already exists on this date")
        }

        // Save and return the new sleep log
        return sleepLogRepository.save(
                SleepLog(
                        username = request.username,
                        date = request.date,
                        timeInBedStart = request.timeInBedStart,
                        timeInBedEnd = request.timeInBedEnd,
                        totalTimeInBed = request.totalTimeInBed,
                        feeling = SleepLog.Feeling.valueOf(request.feeling.uppercase())
                )
        )
    }

    fun getSleepLogsByUsername(username: String): List<SleepLogResponse> {
        // Check if the user exists
        checkUserExists(username)

        val sleepLogs = sleepLogRepository.findByUsername(username)
        return sleepLogs.map { SleepLogResponse.fromEntity(it) }
    }

    fun updateFeelingByUsernameAndDate(username: String, date: LocalDate, feeling: SleepLog.Feeling): SleepLog {
        // Check if the user exists
        checkUserExists(username)

        // Find the sleep log for the given user and date
        val sleepLog = getSleepLogByUsernameAndDate(username, date)

        // Update the feeling
        sleepLog.feeling = feeling

        // Save the updated sleep log
        return sleepLogRepository.save(sleepLog)
    }

    fun updateTimeByUsernameAndDate(username: String, date: LocalDate, timeInBedStart: LocalDateTime, timeInBedEnd: LocalDateTime, totalTimeInBed: Int): SleepLog {
        // Check if the user exists
        checkUserExists(username)

        // Find the sleep log for the given user and date
        val sleepLog = getSleepLogByUsernameAndDate(username, date)

        // Update the time-related fields
        sleepLog.timeInBedStart = timeInBedStart
        sleepLog.timeInBedEnd = timeInBedEnd
        sleepLog.totalTimeInBed = totalTimeInBed

        // Save and return the updated sleep log
        return sleepLogRepository.save(sleepLog)
    }

    fun getSleepLogByUsernameAndDate(username: String, date: LocalDate): SleepLog {
        // Check if the user exists
        checkUserExists(username)

        // Fetch the sleep log for the user on the given date
        return sleepLogRepository.findByUsernameAndDate(username, date)
                ?: throw IllegalArgumentException("No sleep log found for user $username on date $date")
    }

    fun getLastNightSleep(username: String): SleepLogResponse {
        // Check if the user exists
        checkUserExists(username)

        // Calculate the date for last night (yesterday)
        val lastNightDate = LocalDate.now().minusDays(1)

        // Fetch the most recent sleep log for the user on last night's date
        val sleepLog = getSleepLogByUsernameAndDate(username, lastNightDate)

        // Map the sleep log to a response object
        return SleepLogResponse.fromEntity(sleepLog)
    }

    // Get last 30-day averages for a user
    fun getLast30DayAverages(username: String): Last30DayAveragesResponse {
        // Check if the user exists
        checkUserExists(username)

        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(30)

        // Fetch sleep logs for the last 30 days
        val sleepLogs = sleepLogRepository.findByUsernameAndDateBetween(username, startDate, endDate)

        if (sleepLogs.isEmpty()) {
            // If no logs are found, return a response with default values and 0 days included
            return Last30DayAveragesResponse(
                    startDate = startDate,
                    endDate = endDate,
                    averageTotalTimeInBed = 0.0,
                    averageTimeInBedStart = LocalTime.MIN,
                    averageTimeInBedEnd = LocalTime.MIN,
                    feelingFrequencies = emptyMap(),
                    daysIncluded = 0
            )
        }

        // Calculate averages and frequencies
        val averageTotalTimeInBed = sleepLogs.map { it.totalTimeInBed }.average()

        // Calculate the average time in bed start
        val averageTimeInBedStart = sleepLogs
                .map { it.timeInBedStart.toLocalTime().toSecondOfDay() } // Convert to seconds since midnight
                .average()
                .let { LocalTime.ofSecondOfDay(it.toLong()) } // Convert back to LocalTime

        // Calculate the average time in bed end
        val averageTimeInBedEnd = sleepLogs
                .map { it.timeInBedEnd.toLocalTime().toSecondOfDay() } // Convert to seconds since midnight
                .average()
                .let { LocalTime.ofSecondOfDay(it.toLong()) } // Convert back to LocalTime

        // Frequency of feelings
        val feelingFrequencies = sleepLogs.groupingBy { it.feeling }.eachCount()

        val availableDays = sleepLogs.size

        return Last30DayAveragesResponse(
                startDate = startDate,
                endDate = endDate,
                averageTotalTimeInBed = averageTotalTimeInBed,
                averageTimeInBedStart = averageTimeInBedStart,
                averageTimeInBedEnd = averageTimeInBedEnd,
                feelingFrequencies = feelingFrequencies,
                daysIncluded = availableDays
        )
    }
}
