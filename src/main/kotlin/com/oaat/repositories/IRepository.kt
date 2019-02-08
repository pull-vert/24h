package com.oaat.repositories

import com.oaat.entities.Entity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface IRepository<T : Entity> : ReactiveMongoRepository<T, String>
