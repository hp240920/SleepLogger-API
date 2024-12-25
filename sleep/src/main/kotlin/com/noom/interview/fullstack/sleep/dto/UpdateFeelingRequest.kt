package com.noom.interview.fullstack.sleep.dto

import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateFeelingRequest(
        @field:NotBlank(message = "Username is required")
        val username: String,

        @field:NotNull(message = "Date is required")
        val date: LocalDate,

        @field:NotBlank(message = "Feeling is required")
        val feeling: String
)