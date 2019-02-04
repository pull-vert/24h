package com.oaat.services

import com.oaat.entities.User
import com.oaat.repositories.UserRepository
import com.oaat.security.JWTUtil
import com.oaat.web.UnauthorizedStatusException
import com.oaat.web.dtos.AuthRequestDto
import com.oaat.web.dtos.AuthResponseDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toMono

@Service
class UserService(
        override val repository: UserRepository,
        private val jwtUtil: JWTUtil,
        private val passwordEncoder: PasswordEncoder
) : IService<User> {

    override fun save(entity: User) =
            entity.toMono()
                    .doOnNext { user -> user.password = passwordEncoder.encode(user.password) }
                    .flatMap { user -> repository.save(user) }

    fun auth(authRequestDto: AuthRequestDto) =
            repository.findByUsername(authRequestDto.username)
                    .map { user ->
                        if (passwordEncoder.matches(authRequestDto.password, user.password)) {
                            AuthResponseDto(jwtUtil.generateToken(user))
                        } else {
                            throw UnauthorizedStatusException()
                        }
                    }.switchIfEmpty { throw UnauthorizedStatusException() }

    fun findByUsername(username: String) = repository.findByUsername(username)
}
