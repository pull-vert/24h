/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.repositories

import com.oaat.entities.User
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : IRepository<User>, ReactiveUserDetailsService
