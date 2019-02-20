/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat.repositories

import com.oaat.entities.Entity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface IRepository<T : Entity> : ReactiveMongoRepository<T, String>
