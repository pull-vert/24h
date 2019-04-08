/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
        private val authenticationManager: AuthenticationManager,
        private val securityContextRepository: SecurityContextRepository
) {

    /**
     * The default password encoder, used for encoding password in User Document
     * And used to decode password in Spring security Authentication
     */
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    /**
     * Configure endpoints security : roles required, if need authenticated caller...
     */
    @Bean
    fun securitygWebFilterChain(http: ServerHttpSecurity) =
            http
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable()
                    .authenticationManager(authenticationManager)
                    .securityContextRepository(securityContextRepository)
                    .authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .pathMatchers(HttpMethod.POST, "/auth", "/api/users").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/users/checkUsernameAvailability",
                            "/api/users/checkEmailAvailability").permitAll()
                    .pathMatchers(HttpMethod.DELETE, "/api/users/{userId}").hasRole("ADMIN")
                    .pathMatchers("/api/**").hasRole("USER")
                    .anyExchange().authenticated()
                    .and().build()
}
