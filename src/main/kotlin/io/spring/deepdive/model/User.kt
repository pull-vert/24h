/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.deepdive.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import javax.validation.constraints.Email

@Document
data class User(
        @Id private val username: String, // private to avoid conflict with getPassword java method from UserDetails
        private val password: String, // private to avoid conflict with getPassword java method from UserDetails
        @Email val email: String,
        val firstname: String? = null,
        val lastname: String? = null,
        val roles: Set<Role> = setOf(Role.USER), // By Default : Role = USER
        val description: String? = null,
        val active: Boolean = true
) : UserDetails, Persistable<String> {

    @CreatedDate
    lateinit var createdDate: LocalDateTime
    @LastModifiedDate
    lateinit var lastModifiedDate: LocalDateTime

    // Persistable functions
    override fun isNew() = this::createdDate.isInitialized
    override fun getId() = username

    // UserDetails functions
    override fun getAuthorities() = roles
    override fun getUsername() = username
    override fun getPassword() = password
    override fun isEnabled() = active
    override fun isCredentialsNonExpired() = active
    override fun isAccountNonExpired() = active
    override fun isAccountNonLocked() = active

    // AuthenticatedPrincipal function
    override fun getName() = "$username${if (null != firstname) " : $firstname $lastname" else ""}"
}
