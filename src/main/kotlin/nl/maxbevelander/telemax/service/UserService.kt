package nl.maxbevelander.telemax.service

import nl.maxbevelander.telemax.entity.User
import nl.maxbevelander.telemax.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findAll(): List<User> = userRepository.findAll()

    fun findById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun existsByUsername(username: String): Boolean = userRepository.findByUsername(username) != null

    fun create(username: String, password: String, role: String): User {
        val user = User(
            username = username,
            password = passwordEncoder.encode(password)!!,
            role = role,
            enabled = true
        )
        return userRepository.save(user)
    }

    fun delete(user: User) = userRepository.delete(user)

    fun countByRole(role: String): Long = userRepository.countByRole(role)
}
