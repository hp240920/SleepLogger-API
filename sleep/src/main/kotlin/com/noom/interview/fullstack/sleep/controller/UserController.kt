package com.noom.interview.fullstack.sleep.controller

import com.noom.interview.fullstack.sleep.dto.CreateUserRequest
import com.noom.interview.fullstack.sleep.dto.ErrorResponse
import com.noom.interview.fullstack.sleep.dto.UpdateUserRequest
import com.noom.interview.fullstack.sleep.entity.User
import com.noom.interview.fullstack.sleep.exception.UserAlreadyExistsException
import com.noom.interview.fullstack.sleep.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
        private val userService: UserService
) {

    // Create a new user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody createUserRequest: CreateUserRequest): ResponseEntity<Any> {
        return try {
            val user = userService.createUser(createUserRequest)
            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (e: UserAlreadyExistsException) {
            val errorResponse = ErrorResponse(
                    message = e.message ?: "User already exists",
                    error = "Conflict",
                    status = HttpStatus.CONFLICT.value()
            )
            ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)  // 409 Conflict with structured message
        }
    }

    // Get user by username
    @GetMapping("/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<User> {
        val user = userService.getUserByUsername(username)
        return ResponseEntity.ok(user)
    }

    // Get user by email
    @GetMapping("/email")
    fun getUserByEmail(@RequestParam email: String): ResponseEntity<User> {
        val user = userService.getUserByEmail(email)
        return ResponseEntity.ok(user)
    }

    // Get all users
    @GetMapping
    fun getAllUsers(): ResponseEntity<Any> {
        val users = userService.getAllUsers()
        return if (users.isNotEmpty()) {
            ResponseEntity.ok(users)
        } else {
            val errorResponse = mapOf("message" to "No users found")
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse)
        }
    }

    // Update user's name or email
    @PutMapping("/{username}")
    fun updateUser(
            @PathVariable username: String,
            @RequestBody updateUserRequest: UpdateUserRequest
    ): ResponseEntity<Any> {
        val updatedUser = userService.updateUser(username, updateUserRequest)
        return ResponseEntity.ok(updatedUser)
    }
}
