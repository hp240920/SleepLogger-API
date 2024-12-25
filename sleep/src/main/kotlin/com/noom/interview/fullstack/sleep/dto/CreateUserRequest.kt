package com.noom.interview.fullstack.sleep.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateUserRequest(
        @field:NotBlank(message = "Name is required")
        @field:Size(max = 255, message = "Name cannot exceed 255 characters")
        val name: String,

        @field:NotBlank(message = "Email is required")
        @field:Email(message = "Email should be valid")
        @field:Size(max = 255, message = "Email cannot exceed 255 characters")
        val email: String,

        @field:NotBlank(message = "Username is required")
        @field:Size(max = 255, message = "Username cannot exceed 255 characters")
        val username: String
)
