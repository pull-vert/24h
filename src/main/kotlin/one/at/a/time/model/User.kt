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
package one.at.a.time.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.AuthenticatedPrincipal
import org.springframework.security.core.userdetails.UserDetails
import javax.validation.constraints.Email

@Document
data class User(
        var username: String, // internal to avoid conflict with getPassword java method from UserDetails
        var password: String, // internal to avoid conflict with getPassword java method from UserDetails
        var roles: MutableList<Role> = mutableListOf(Role.USER), // Default : USER
        var active: Boolean = true
) : Auditable()