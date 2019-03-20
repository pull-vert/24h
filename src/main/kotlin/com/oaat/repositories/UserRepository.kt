/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.repositories

import com.oaat.entities.User
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : IRepository<User>, ReactiveUserDetailsService {

    /**
     * Find the [User] by username.
     *
     * @param email the email to look up
     * @return the [User]. Cannot be null
     */
    fun findByEmail(email: String): Mono<User>
}
