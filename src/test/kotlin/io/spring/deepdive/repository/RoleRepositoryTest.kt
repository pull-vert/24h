package io.spring.deepdive.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.test

@ExtendWith(SpringExtension::class)
@SpringBootTest
class RoleRepositoryTest(@Autowired val roleRepository: RoleRepository) {

    @Test
    fun `Assert findById returns expected Document`() {
        roleRepository.findById("user")
                .test()
                .consumeNextWith {
                    assertThat(it.role).isEqualTo("user")
                }.verifyComplete()
    }
}