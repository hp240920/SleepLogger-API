package com.noom.interview.fullstack.sleep.service

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequest
import com.noom.interview.fullstack.sleep.entity.SleepLog
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository
import com.noom.interview.fullstack.sleep.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class SleepLogServiceTest {

    private lateinit var sleepLogService: SleepLogService
    private lateinit var sleepLogRepository: SleepLogRepository
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        sleepLogRepository = mock(SleepLogRepository::class.java)
        userRepository = mock(UserRepository::class.java)
        sleepLogService = SleepLogService(sleepLogRepository, userRepository)
    }

    @Test
    fun `should create sleep log`() {
        val request = CreateSleepLogRequest(
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = "GOOD"
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsernameAndDate("testuser", request.date)).thenReturn(null)
        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenAnswer { it.arguments[0] }

        val result = sleepLogService.createSleepLog(request)

        assertEquals("testuser", result.username)
        assertEquals(480, result.totalTimeInBed)
        verify(sleepLogRepository, times(1)).save(any(SleepLog::class.java))
    }

    @Test
    fun `should throw exception when user does not exist`() {
        val request = CreateSleepLogRequest(
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = "GOOD"
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(false)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            sleepLogService.createSleepLog(request)
        }

        assertEquals("User with username testuser does not exist", exception.message)
    }

    @Test
    fun `should get sleep logs by username`() {
        val sleepLog = SleepLog(
                id = UUID.randomUUID(),
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = SleepLog.Feeling.GOOD
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsername("testuser")).thenReturn(listOf(sleepLog))

        val result = sleepLogService.getSleepLogsByUsername("testuser")

        assertEquals(1, result.size)
        assertEquals("testuser", result[0].username)
    }

    @Test
    fun `should update feeling by username and date`() {
        val sleepLog = SleepLog(
                id = UUID.randomUUID(),
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = SleepLog.Feeling.GOOD
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsernameAndDate("testuser", LocalDate.now())).thenReturn(sleepLog)
        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenAnswer { it.arguments[0] }

        val result = sleepLogService.updateFeelingByUsernameAndDate("testuser", LocalDate.now(), SleepLog.Feeling.OK)

        assertEquals(SleepLog.Feeling.OK, result.feeling)
        verify(sleepLogRepository, times(1)).save(sleepLog)
    }

    @Test
    fun `should update time by username and date`() {
        val sleepLog = SleepLog(
                id = UUID.randomUUID(),
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = SleepLog.Feeling.GOOD
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsernameAndDate("testuser", LocalDate.now())).thenReturn(sleepLog)
        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenAnswer { it.arguments[0] }

        val updatedStart = LocalDateTime.of(2024, 12, 23, 21, 0)
        val updatedEnd = LocalDateTime.of(2024, 12, 24, 5, 30)
        val updatedTotal = 510

        val result = sleepLogService.updateTimeByUsernameAndDate("testuser", LocalDate.now(), updatedStart, updatedEnd, updatedTotal)

        assertEquals(updatedStart, result.timeInBedStart)
        assertEquals(updatedEnd, result.timeInBedEnd)
        assertEquals(updatedTotal, result.totalTimeInBed)
        verify(sleepLogRepository, times(1)).save(sleepLog)
    }

    @Test
    fun `should get sleep log by username and date`() {
        val sleepLog = SleepLog(
                id = UUID.randomUUID(),
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = SleepLog.Feeling.GOOD
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsernameAndDate("testuser", LocalDate.now())).thenReturn(sleepLog)

        val result = sleepLogService.getSleepLogByUsernameAndDate("testuser", LocalDate.now())

        assertEquals("testuser", result.username)
        assertEquals(LocalDate.now(), result.date)
        assertEquals(480, result.totalTimeInBed)
    }

    @Test
    fun `should get last night's sleep log`() {
        val sleepLog = SleepLog(
                id = UUID.randomUUID(),
                username = "testuser",
                date = LocalDate.now().minusDays(1),
                timeInBedStart = LocalDateTime.of(2024, 12, 22, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 23, 6, 0),
                totalTimeInBed = 480,
                feeling = SleepLog.Feeling.GOOD
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsernameAndDate("testuser", LocalDate.now().minusDays(1))).thenReturn(sleepLog)

        val result = sleepLogService.getLastNightSleep("testuser")

        assertEquals("testuser", result.username)
        assertEquals(LocalDate.now().minusDays(1), result.date)
    }

    @Test
    fun `should get last 30-day averages`() {
        val sleepLogs = listOf(
                SleepLog(
                        id = UUID.randomUUID(),
                        username = "testuser",
                        date = LocalDate.now().minusDays(1),
                        timeInBedStart = LocalDateTime.of(2024, 12, 22, 22, 0),
                        timeInBedEnd = LocalDateTime.of(2024, 12, 23, 6, 0),
                        totalTimeInBed = 480,
                        feeling = SleepLog.Feeling.GOOD
                )
        )

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsernameAndDateBetween("testuser", LocalDate.now().minusDays(30), LocalDate.now())).thenReturn(sleepLogs)

        val result = sleepLogService.getLast30DayAverages("testuser")

        assertEquals(1, result.daysIncluded)
        assertEquals(480.0, result.averageTotalTimeInBed)
    }

    @Test
    fun `should get last 30-day averages with no logs`() {
        val sleepLogs = emptyList<SleepLog>()

        `when`(userRepository.existsByUsername("testuser")).thenReturn(true)
        `when`(sleepLogRepository.findByUsernameAndDateBetween("testuser", LocalDate.now().minusDays(30), LocalDate.now())).thenReturn(sleepLogs)

        val result = sleepLogService.getLast30DayAverages("testuser")

        assertEquals(0, result.daysIncluded)
        assertEquals(0.0, result.averageTotalTimeInBed)
    }
}
