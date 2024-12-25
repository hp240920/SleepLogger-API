package com.noom.interview.fullstack.sleep.service

import com.noom.interview.fullstack.sleep.dto.CreateUserRequest
import com.noom.interview.fullstack.sleep.dto.UpdateUserRequest
import com.noom.interview.fullstack.sleep.entity.User
import com.noom.interview.fullstack.sleep.exception.UserAlreadyExistsException
import com.noom.interview.fullstack.sleep.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository
) {

    // Create a new user
    fun createUser(request: CreateUserRequest): User {
        // Check if a user with the same username or email already exists
        val existingUserByUsername = userRepository.findByUsername(request.username)
        val existingUserByEmail = userRepository.findByEmail(request.email)

        if (existingUserByUsername.isPresent) {
            throw UserAlreadyExistsException("User with username ${request.username} already exists")
        }
        if (existingUserByEmail.isPresent) {
            throw UserAlreadyExistsException("User with email ${request.email} already exists")
        }

        val user = User(
                name = request.name,
                email = request.email,
                username = request.username
        )
        return userRepository.save(user)
    }

    // Get user by ID
    fun getUserByUsername(username: String): User {
        return userRepository.findByUsername(username)
                .orElseThrow { NoSuchElementException("User with username $username not found") }
    }

    // Get user by email
    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
                .orElseThrow { NoSuchElementException("User with username $email not found") }
    }

    // Get all users
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun updateUser(username: String, updateUserRequest: UpdateUserRequest): User {
        val user = userRepository.findByUsername(username)
                .orElseThrow { NoSuchElementException("User with ID $username not found") }

        updateUserRequest.name?.let { user.name = it }
        updateUserRequest.email?.let { user.email = it }

        return userRepository.save(user)
    }
}
