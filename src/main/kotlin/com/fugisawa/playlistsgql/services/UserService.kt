package com.fugisawa.playlistsgql.services

import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import java.util.UUID

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun getById(id: UUID): User? = userRepository.getById(id)

    suspend fun getByIds(ids: List<UUID>): List<User> = userRepository.getByIds(ids)

    suspend fun getAll(): List<User> = userRepository.getAll()

    suspend fun findByUsername(username: String): User? = userRepository.findByUsername(username)

    suspend fun create(user: User): User = userRepository.create(user)

    suspend fun update(user: User): User = userRepository.update(user)

    suspend fun delete(id: UUID): Boolean = userRepository.delete(id)
}
