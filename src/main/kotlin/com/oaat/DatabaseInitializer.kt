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
package com.oaat

import com.oaat.entities.Message
import com.oaat.entities.MessageEvent
import com.oaat.entities.Role.ROLE_ADMIN
import com.oaat.entities.Role.ROLE_USER
import com.oaat.entities.User
import com.oaat.repositories.MessageRepository
import com.oaat.services.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.toFlux

internal const val USER_FRED_UUID = "79e9eb45-2835-49c8-ad3b-c951b591bc7f"
internal const val USER_BOSS_UUID = "67d4306e-d99d-4e54-8b1d-5b1e92691a4e"
internal const val REACTOR_IS_OUT_UUID = "66c52f38-c240-4776-96ec-ee6ada201c2e"

@Component
class DatabaseInitializer(
        private val ops: MongoOperations,
        private val userService: UserService,
        private val messageRepository: MessageRepository) : CommandLineRunner {

    override fun run(vararg args: String) {
        println("DatabaseInitializer start")

        // uncomment if targetting a real MongoDB Database (not embedded)
//        ops.dropCollection(MessageEvent::class.java)
//        userService.deleteAll()
//                .block()
//        messageRepository.deleteAll()
//                .block()

        ops.createCollection(MessageEvent::class.java, CollectionOptions.empty().capped().size(10000))

        val fred = User("Fred", "password", id = USER_FRED_UUID, enabled = true)
        val boss = User("Boss", "secured_password", mutableListOf(ROLE_ADMIN, ROLE_USER), id = USER_BOSS_UUID, enabled = true)
        listOf(fred, boss)
                .toFlux()
                .flatMap { user -> userService.save(user) }
                .doOnNext { user -> println("saved User $user, entity informations; createdBy=${user.createdBy}, createdDate=${user.createdDate}") }
                .blockLast()


        val reactorTitle = "Reactor Bismuth is out"
        val reactorPost = Message(
                reactorTitle,
                reactorTitle.slugify(),
                """It is my great pleasure to announce the GA release of **Reactor Bismuth**, which notably encompasses
                    |`reactor-core` **3.1.0.RELEASE** and `reactor-netty` **0.7.0.RELEASE** \uD83C\uDF89""".trimMargin(),
                "simonbasle",
                id = REACTOR_IS_OUT_UUID
        )

        val springTitle = "Spring Framework 5.0 goes GA"
        val spring5Post = Message(
                springTitle,
                springTitle.slugify(),
                """Dear Spring community,
                    |It is my pleasure to announce that, after more than a year of milestones and RCs and almost two years of development overall,
                    |Spring Framework 5.0 is finally generally available as 5.0.0.RELEASE from [repo.spring.io](https://repo.spring.io)
                    |and Maven Central!""".trimMargin(),
                "springjuergen"
        )

        val postTitle = "Introducing Kotlin support in Spring Framework 5.0"
        val springKotlinPost = Message(
                postTitle,
                postTitle.slugify(),
                """One of the key strengths of Kotlin is that it provides a very good [interoperability](https://kotlinlang.org/docs/reference/java-interop.html)
                    |with libraries written in Java. But there are ways to go even further and allow writing fully idiomatic Kotlin code when developing your next
                    |Spring application. In addition to Spring Framework support for Java 8 that Kotlin applications can leverage like functional web or bean registration APIs,
                    |there are additional Kotlin dedicated features that should allow you to reach a new level of productivity.""".trimMargin(),
                "sdeleuze"
        )

        listOf(reactorPost, spring5Post, springKotlinPost)
                .toFlux()
                .flatMap { message -> messageRepository.save(message) }
                .doOnNext { message -> println("saved Message $message") }
                .blockLast()
    }
}
