package com.oaat.repositories

import com.oaat.entities.Entity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface IRepository<T : Entity> : ReactiveMongoRepository<T, String>
