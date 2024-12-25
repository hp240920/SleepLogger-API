package com.noom.interview.fullstack.sleep.service

import com.noom.interview.fullstack.sleep.dto.CreateUserRequest
import com.noom.interview.fullstack.sleep.dto.UpdateUserRequest
import com.noom.interview.fullstack.sleep.entity.User
import com.noom.interview.fullstack.sleep.exception.UserAlreadyExistsException
import com.noom.interview.fullstack.sleep.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

class UserServiceTest {

    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        userService = UserService(userRepository)
    }

    @Test
    fun `should create a new user`() {
        val request = CreateUserRequest(
                name = "John Doe",
                email = "john.doe@example.com",
                username = "johndoe"
        )

        val savedUser = User(
                id = UUID.randomUUID(),
                name = "John Doe",
                email = "john.doe@example.com",
                username = "johndoe"
        )

        `when`(userRepository.findByUsername(request.username)).thenReturn(Optional.empty())
        `when`(userRepository.findByEmail(request.email)).thenReturn(Optional.empty())
        `when`(userRepository.save(any(User::class.java))).thenReturn(savedUser)

        val result = userService.createUser(request)

        assertEquals("John Doe", result.name)
        assertEquals("john.doe@example.com", result.email)
        assertEquals("johndoe", result.username)

        verify(userRepository, times(1)).findByUsername(request.username)
        verify(userRepository, times(1)).findByEmail(request.email)
        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun `should throw exception if username already exists`() {
        val request = CreateUserRequest(
                name = "John Doe",
                email = "john.doe@example.com",
                username = "johndoe"
        )

        `when`(userRepository.findByUsername(request.username)).thenReturn(Optional.of(User(
                id = UUID.randomUUID(),
                name = "Existing User",
                email = "existing@example.com",
                username = "johndoe"
        )))

        val exception = assertThrows<UserAlreadyExistsException> {
            userService.createUser(request)
        }

        assertEquals("User with username johndoe already exists", exception.message)
        verify(userRepository, times(1)).findByUsername(request.username)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `should throw exception if email already exists`() {
        val request = CreateUserRequest(
                name = "John Doe",
                email = "john.doe@example.com",
                username = "johndoe"
        )

        `when`(userRepository.findByEmail(request.email)).thenReturn(Optional.of(User(
                id = UUID.randomUUID(),
                name = "Existing User",
                email = "john.doe@example.com",
                username = "username"
        )))

        val exception = assertThrows<UserAlreadyExistsException> {
            userService.createUser(request)
        }

        assertEquals("User with email john.doe@example.com already exists", exception.message)
        verify(userRepository, times(1)).findByEmail(request.email)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `should get user by username`() {
        val username = "johndoe"
        val user = User(
                id = UUID.randomUUID(),
                name = "John Doe",
                email = "john.doe@example.com",
                username = username
        )

        `when`(userRepository.findByUsername(username)).thenReturn(Optional.of(user))

        val result = userService.getUserByUsername(username)

        assertEquals("John Doe", result.name)
        assertEquals("john.doe@example.com", result.email)
        assertEquals(username, result.username)

        verify(userRepository, times(1)).findByUsername(username)
    }

    @Test
    fun `should get user by email`() {
        val email = "john.doe@example.com"
        val user = User(
                id = UUID.randomUUID(),
                name = "John Doe",
                email = "john.doe@example.com",
                username = "username"
        )

        `when`(userRepository.findByEmail(email)).thenReturn(Optional.of(user))

        val result = userService.getUserByEmail(email)

        assertEquals("John Doe", result.name)
        assertEquals("john.doe@example.com", result.email)
        assertEquals("username", result.username)

        verify(userRepository, times(1)).findByEmail(email)
    }

    @Test
    fun `should get all users`() {
        val email = "john.doe@example.com"
        val user = User(
                id = UUID.randomUUID(),
                name = "John Doe",
                email = "john.doe@example.com",
                username = "username"
        )

        `when`(userRepository.findAll()).thenReturn(listOf(user))

        val result = userService.getAllUsers()

        assertEquals("John Doe", result[0].name)
        assertEquals("john.doe@example.com", result[0].email)
        assertEquals("username", result[0].username)

        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun `should throw exception if user not found by username`() {
        val username = "nonexistent"

        `when`(userRepository.findByUsername(username)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> {
            userService.getUserByUsername(username)
        }

        assertEquals("User with username $username not found", exception.message)
        verify(userRepository, times(1)).findByUsername(username)
    }

    @Test
    fun `should update user`() {
        val username = "johndoe"
        val user = User(
                id = UUID.randomUUID(),
                name = "John Doe",
                email = "john.doe@example.com",
                username = username
        )
        val updateRequest = UpdateUserRequest(
                name = "Johnathan Doe",
                email = "johnathan.doe@example.com"
        )

        `when`(userRepository.findByUsername(username)).thenReturn(Optional.of(user))
        `when`(userRepository.save(any(User::class.java))).thenReturn(user.apply {
            name = updateRequest.name!!
            email = updateRequest.email!!
        })

        val result = userService.updateUser(username, updateRequest)

        assertEquals("Johnathan Doe", result.name)
        assertEquals("johnathan.doe@example.com", result.email)

        verify(userRepository, times(1)).findByUsername(username)
        verify(userRepository, times(1)).save(any(User::class.java))
    }
}
