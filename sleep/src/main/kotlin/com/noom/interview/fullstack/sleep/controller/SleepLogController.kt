package com.noom.interview.fullstack.sleep.controller

import com.noom.interview.fullstack.sleep.dto.*
import com.noom.interview.fullstack.sleep.entity.SleepLog
import com.noom.interview.fullstack.sleep.service.SleepLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/sleep-logs")
class SleepLogController @Autowired constructor(
        private val sleepLogService: SleepLogService
) {

    @PostMapping
    fun createSleepLog(@RequestBody @Valid request: CreateSleepLogRequest): ResponseEntity<Any> {
        // Validate the time range
        if (request.timeInBedStart.isAfter(request.timeInBedEnd)) {
            throw IllegalArgumentException("Start time cannot be after end time")
        }

        // Create the sleep log
        val sleepLog = sleepLogService.createSleepLog(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(sleepLog)
    }

    @GetMapping("/user/{username}")
    fun getSleepLogsByUsername(@PathVariable username: String): List<SleepLogResponse> {
        return sleepLogService.getSleepLogsByUsername(username)
    }

    @PatchMapping("/sleep-log/update-feeling")
    fun updateFeelingByUsernameAndDate(
            @RequestBody request: UpdateFeelingRequest
    ): ResponseEntity<SleepLog> {
        val sleepLog = sleepLogService.updateFeelingByUsernameAndDate(
                request.username,
                request.date,
                SleepLog.Feeling.valueOf(request.feeling.uppercase())
        )
        return ResponseEntity.ok(sleepLog)
    }

    @PatchMapping("/sleep-log/update-time")
    fun updateTimeByUsernameAndDate(
            @RequestBody request: UpdateTimeRequest
    ): ResponseEntity<SleepLog> {
        val sleepLog = sleepLogService.updateTimeByUsernameAndDate(
                request.username,
                request.date,
                request.timeInBedStart,
                request.timeInBedEnd,
                request.totalTimeInBed
        )
        return ResponseEntity.ok(sleepLog)
    }

    @GetMapping("/sleep-log/last-night/{username}")
    fun getLastNightSleep(@PathVariable username: String): SleepLogResponse {
        return sleepLogService.getLastNightSleep(username)
    }

    // Endpoint to get a single sleep log by username and date
    @GetMapping("/view")
    fun getSleepLog(
            @RequestParam username: String,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate
    ): SleepLogResponse {
        val sleepLog = sleepLogService.getSleepLogByUsernameAndDate(username, date)
        return SleepLogResponse.fromEntity(sleepLog)
    }

    // Endpoint to get the last 30-day averages
    @GetMapping("/sleep-log/averages/{username}")
    fun getLast30DayAverages(@PathVariable username: String): Last30DayAveragesResponse {
        return sleepLogService.getLast30DayAverages(username)
    }
}
