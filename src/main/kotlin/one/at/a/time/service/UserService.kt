package one.at.a.time.service

import one.at.a.time.repository.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserService(val userRepository: UserRepository) {
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(id: UUID) = userRepository.deleteById(id)
}