package com.fugisawa.playlistsgql.services

import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import com.fugisawa.playlistsgql.infrastructure.security.PasswordUtils
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun getById(id: UUID): User? = userRepository.getById(id)

    suspend fun getByIds(ids: List<UUID>): List<User> = userRepository.getByIds(ids)

    suspend fun getAll(): List<User> = userRepository.getAll()

    suspend fun findByUsername(username: String): User? = userRepository.findByUsername(username)

    suspend fun create(user: User): User = userRepository.create(user)

    suspend fun createWithPassword(
        username: String,
        password: String,
    ): User {
        val passwordHash = hashPassword(password)
        val user =
            User(
                username = username,
                passwordHash = passwordHash,
            )
        return create(user)
    }

    suspend fun update(user: User): User = userRepository.update(user)

    suspend fun updateWithPassword(
        user: User,
        password: String?,
    ): User {
        val updatedUser =
            if (password != null) {
                user.copy(passwordHash = hashPassword(password))
            } else {
                user
            }
        return update(updatedUser)
    }

    suspend fun delete(id: UUID): Boolean = userRepository.delete(id)

    fun hashPassword(password: String): String = PasswordUtils.hashPassword(password)

    fun verifyPassword(
        password: String,
        passwordHash: String,
    ): Boolean = PasswordUtils.verifyPassword(password, passwordHash)
}
