package com.noom.interview.fullstack.sleep.repository

import com.noom.interview.fullstack.sleep.entity.SleepLog
import com.noom.interview.fullstack.sleep.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.*

interface SleepLogRepository : JpaRepository<SleepLog, UUID> {
    fun findByUsernameAndDate(username: String, date: LocalDate): SleepLog?
    fun findByUsername(username: String): List<SleepLog>
    fun findByUsernameAndDateBetween(username: String, startDate: LocalDate, endDate: LocalDate): List<SleepLog>
}
