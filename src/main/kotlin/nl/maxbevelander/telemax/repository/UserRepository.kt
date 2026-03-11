package nl.maxbevelander.telemax.repository

import nl.maxbevelander.telemax.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
