package io.spring.deepdive.repository

import io.spring.deepdive.model.Role.ADMIN
import io.spring.deepdive.model.Role.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserRepositoryTest(@Autowired val userRepository: UserRepository) {

    @Test
    fun `Assert findById returns expected Document`() {
        userRepository.findById("sdeleuze")
                .test()
                .consumeNextWith {
                    assertThat(it.lastname).isEqualTo("Deleuze")
                    assertThat(it.name).isEqualTo("sdeleuze : Sebastien Deleuze")
                    assertThat(it.roles).hasSize(2).containsExactly(USER, ADMIN)
                    assertThat(it.createdDate).isNotNull()
                    assertThat(it.lastModifiedDate).isNotNull()
                }.verifyComplete()
    }

    @Test
    fun `Assert findById returns expected min Document`() {
        userRepository.findById("min")
                .test()
                .consumeNextWith {
                    assertThat(it.lastname).isNull()
                    assertThat(it.name).isEqualTo("min")
                    assertThat(it.roles).hasSize(1).containsExactly(USER)
//                    assertThat(it.createdDate).isNotNull()
//                    assertThat(it.lastModifiedDate).isNotNull()
                }.verifyComplete()
    }
}