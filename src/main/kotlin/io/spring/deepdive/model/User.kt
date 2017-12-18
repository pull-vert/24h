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
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import javax.validation.constraints.Email

@Document
data class User(
        @Id private val username: String, // private to avoid conflict with getPassword java method from UserDetails
        private val password: String, // private to avoid conflict with getPassword java method from UserDetails
        val firstname: String,
        val lastname: String,
        @Email val email: String,
        val roles: Set<Role>,
        val description: String? = null,
        val active: Boolean = true,
        @CreatedDate val addedAt: LocalDateTime = LocalDateTime.now()) : UserDetails {
    override fun getAuthorities() = roles

    override fun getUsername() = username
    override fun getPassword() = password

    override fun getName() = "$firstname $lastname"

    override fun isEnabled() = active
    override fun isCredentialsNonExpired() = active
    override fun isAccountNonExpired() = active
    override fun isAccountNonLocked() = active
}
