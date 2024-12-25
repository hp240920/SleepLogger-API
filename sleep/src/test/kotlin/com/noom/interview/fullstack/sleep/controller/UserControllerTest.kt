package com.noom.interview.fullstack.sleep.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.noom.interview.fullstack.sleep.dto.CreateUserRequest
import com.noom.interview.fullstack.sleep.dto.UpdateUserRequest
import com.noom.interview.fullstack.sleep.entity.User
import com.noom.interview.fullstack.sleep.exception.UserAlreadyExistsException
import com.noom.interview.fullstack.sleep.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("Unittest")
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `createUser should return 201 when user is created`() {
        val createUserRequest = CreateUserRequest(name = "John Doe", email = "john.doe@example.com", username = "johndoe")
        val user = User(id = UUID.randomUUID(), name = "John Doe", email = "john.doe@example.com", username = "johndoe")

        Mockito.`when`(userService.createUser(createUserRequest)).thenReturn(user)

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.username").value("johndoe"))
    }

    @Test
    fun `should return conflict when user already exists`() {
        val createUserRequest = CreateUserRequest(
                name = "John Doe",
                email = "john.doe@example.com",
                username = "johndoe"
        )

        Mockito.`when`(userService.createUser(createUserRequest))
                .thenThrow(UserAlreadyExistsException("User already exists"))

        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest))
        )
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.message").value("User already exists"))
                .andExpect(jsonPath("$.error").value("Conflict"))
    }

    @Test
    fun `getUserByUsername should return user`() {
        val username = "johndoe"
        val user = User(id = UUID.randomUUID(), name = "John Doe", email = "john.doe@example.com", username = username)

        Mockito.`when`(userService.getUserByUsername(username)).thenReturn(user)

        mockMvc.perform(get("/api/users/{username}", username))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.username").value(username))
    }

    @Test
    fun `getUserByEmail should return user`() {
        val email = "john.doe@example.com"
        val user = User(id = UUID.randomUUID(), name = "John Doe", email = email, username = "johndoe")

        Mockito.`when`(userService.getUserByEmail(email)).thenReturn(user)

        mockMvc.perform(get("/api/users/email").param("email", email))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.username").value("johndoe"))
    }

    @Test
    fun `getAllUsers should return list of users`() {
        val users = listOf(
                User(id = UUID.randomUUID(), name = "John Doe", email = "john.doe@example.com", username = "johndoe"),
                User(id = UUID.randomUUID(), name = "Jane Smith", email = "jane.smith@example.com", username = "janesmith")
        )

        Mockito.`when`(userService.getAllUsers()).thenReturn(users)

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
    }

    @Test
    fun `should return no content when no users are found`() {
        Mockito.`when`(userService.getAllUsers()).thenReturn(emptyList())

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$.message").value("No users found"))
    }

    @Test
    fun `updateUser should return updated user`() {
        val username = "johndoe"
        val updateUserRequest = UpdateUserRequest(name = "John Updated", email = "john.updated@example.com")
        val updatedUser = User(id = UUID.randomUUID(), name = "John Updated", email = "john.updated@example.com", username = username)

        Mockito.`when`(userService.updateUser(username, updateUserRequest)).thenReturn(updatedUser)

        mockMvc.perform(put("/api/users/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
                .andExpect(jsonPath("$.username").value(username))
    }
}
