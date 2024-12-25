package com.noom.interview.fullstack.sleep.entity

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "sleep_logs")
data class SleepLog(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: UUID? = null,

        @Column(nullable = false)
        val username: String, // Reference the user by username instead of userId

        @Column(nullable = false)
        val date: LocalDate,

        @Column(nullable = false)
        var timeInBedStart: LocalDateTime,

        @Column(nullable = false)
        var timeInBedEnd: LocalDateTime,

        @Column(nullable = false)
        var totalTimeInBed: Int,

        @Column(nullable = false)
        var feeling: Feeling
) {
    enum class Feeling {
        BAD, OK, GOOD
    }

    override fun toString(): String {
        return "SleepLog(id=$id, date=$date, timeInBedStart=$timeInBedStart, timeInBedEnd=$timeInBedEnd, totalTimeInBed=$totalTimeInBed, feeling=$feeling)"
    }
}
