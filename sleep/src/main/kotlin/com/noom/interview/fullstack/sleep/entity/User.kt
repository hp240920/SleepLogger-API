package com.noom.interview.fullstack.sleep.entity

import javax.persistence.*
import java.util.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: UUID? = null,

        @Column(nullable = false)
        var name: String,

        @Column(nullable = false, unique = true)
        var email: String,

        @Column(nullable = false, unique = true)
        val username: String // the primary reference in APIs
)