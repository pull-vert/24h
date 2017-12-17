package io.spring.deepdive.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import java.time.LocalDateTime

val roleUser = Role("user")
val roleAdmin = Role("admin")

@Document
data class Role(
        @Id val role: String,
        val addedAt: LocalDateTime = LocalDateTime.now()) : GrantedAuthority {
    override fun getAuthority() = role
}