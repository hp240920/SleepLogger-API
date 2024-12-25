package com.noom.interview.fullstack.sleep.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.noom.interview.fullstack.sleep.dto.*
import com.noom.interview.fullstack.sleep.entity.SleepLog
import com.noom.interview.fullstack.sleep.service.SleepLogService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SleepLogControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sleepLogService: SleepLogService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var sleepLog: SleepLog
    private lateinit var createSleepLogRequest: CreateSleepLogRequest
    private lateinit var updateFeelingRequest: UpdateFeelingRequest
    private lateinit var updateTimeRequest: UpdateTimeRequest

    @BeforeEach
    fun setUp() {
        sleepLog = SleepLog(
                id = UUID.randomUUID(),
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = SleepLog.Feeling.GOOD
        )

        createSleepLogRequest = CreateSleepLogRequest(
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0),
                totalTimeInBed = 480,
                feeling = "GOOD"
        )

        updateFeelingRequest = UpdateFeelingRequest(
                username = "testuser",
                date = LocalDate.now(),
                feeling = "OK"
        )

        updateTimeRequest = UpdateTimeRequest(
                username = "testuser",
                date = LocalDate.now(),
                timeInBedStart = LocalDateTime.of(2024, 12, 23, 23, 0),
                timeInBedEnd = LocalDateTime.of(2024, 12, 24, 7, 0),
                totalTimeInBed = 480
        )
    }

    @Test
    fun `should create a sleep log`() {
        `when`(sleepLogService.createSleepLog(createSleepLogRequest)).thenReturn(sleepLog)

        mockMvc.perform(
                post("/api/sleep-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSleepLogRequest))
        )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.feeling").value("GOOD"))

        verify(sleepLogService, times(1)).createSleepLog(createSleepLogRequest)
    }

    @Test
    fun `should get sleep logs by username`() {
        `when`(sleepLogService.getSleepLogsByUsername("testuser")).thenReturn(listOf(SleepLogResponse.fromEntity(sleepLog)))

        mockMvc.perform(
                get("/api/sleep-logs/user/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[0].username").value("testuser"))

        verify(sleepLogService, times(1)).getSleepLogsByUsername("testuser")
    }

    @Test
    fun `should update feeling by username and date`() {
        `when`(sleepLogService.updateFeelingByUsernameAndDate("testuser", LocalDate.now(), SleepLog.Feeling.OK))
                .thenReturn(sleepLog.copy(feeling = SleepLog.Feeling.OK))

        mockMvc.perform(
                patch("/api/sleep-logs/sleep-log/update-feeling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateFeelingRequest))
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.feeling").value("OK"))

        verify(sleepLogService, times(1)).updateFeelingByUsernameAndDate("testuser", LocalDate.now(), SleepLog.Feeling.OK)
    }

    @Test
    fun `should update time by username and date`() {
        `when`(sleepLogService.updateTimeByUsernameAndDate(
                "testuser",
                LocalDate.now(),
                updateTimeRequest.timeInBedStart,
                updateTimeRequest.timeInBedEnd,
                updateTimeRequest.totalTimeInBed
        )).thenReturn(sleepLog)

        mockMvc.perform(
                patch("/api/sleep-logs/sleep-log/update-time")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTimeRequest))
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.totalTimeInBed").value(480))

        verify(sleepLogService, times(1)).updateTimeByUsernameAndDate(
                "testuser",
                LocalDate.now(),
                updateTimeRequest.timeInBedStart,
                updateTimeRequest.timeInBedEnd,
                updateTimeRequest.totalTimeInBed
        )
    }

    @Test
    fun `should get last night's sleep log`() {
        `when`(sleepLogService.getLastNightSleep("testuser")).thenReturn(SleepLogResponse.fromEntity(sleepLog))

        mockMvc.perform(
                get("/api/sleep-logs/sleep-log/last-night/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.username").value("testuser"))

        verify(sleepLogService, times(1)).getLastNightSleep("testuser")
    }

    @Test
    fun `should get sleep log by username and date`() {
        `when`(sleepLogService.getSleepLogByUsernameAndDate("testuser", LocalDate.now()))
                .thenReturn(sleepLog)

        mockMvc.perform(
                get("/api/sleep-logs/view")
                        .param("username", "testuser")
                        .param("date", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.username").value("testuser"))

        verify(sleepLogService, times(1)).getSleepLogByUsernameAndDate("testuser", LocalDate.now())
    }

    @Test
    fun `should get last 30-day averages`() {
        val averagesResponse = Last30DayAveragesResponse(
                startDate = LocalDate.now().minusDays(30),
                endDate = LocalDate.now(),
                averageTotalTimeInBed = 450.0,
                averageTimeInBedStart = LocalDateTime.of(2024, 12, 23, 22, 0).toLocalTime(),
                averageTimeInBedEnd = LocalDateTime.of(2024, 12, 24, 6, 0).toLocalTime(),
                feelingFrequencies = mapOf(SleepLog.Feeling.GOOD to 20, SleepLog.Feeling.OK to 10),
                daysIncluded = 30
        )

        `when`(sleepLogService.getLast30DayAverages("testuser")).thenReturn(averagesResponse)

        mockMvc.perform(
                get("/api/sleep-logs/sleep-log/averages/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.daysIncluded").value(30))

        verify(sleepLogService, times(1)).getLast30DayAverages("testuser")
    }
}
