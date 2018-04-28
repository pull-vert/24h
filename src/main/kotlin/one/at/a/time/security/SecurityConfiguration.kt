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
package one.at.a.time.security

import one.at.a.time.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository


@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    /**
     * The default password encoder, used for encoding password in User Document
     * And used to decode password in Spring security Authentication
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(
            http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
                .authorizeExchange()
//                .pathMatchers("/admin").hasRole("ADMIN") todo example, uncomment if endpoints needs ADMIN role
//                .pathMatchers("/auth/**").permitAll() todo, uncomment for endpoints that do not require authenticated user
                .anyExchange()
                    .authenticated()
                    .and()
                .httpBasic().securityContextRepository(WebSessionServerSecurityContextRepository())
                    .and()
                .build()
    }

    @Bean
    fun userDetailsService(users: UserRepository): ReactiveUserDetailsService = ReactiveUserDetailsService {
        println("userDetailsService trying to find by username=$it")
        users.findByUsername(it)
                .map { u ->
                    User.withUsername(u.username)
                            .password(u.password)
                            .authorities(u.roles)
                            .accountExpired(!u.active)
                            .credentialsExpired(!u.active)
                            .disabled(!u.active)
                            .accountLocked(!u.active)
                            .build()
                }
    }
}