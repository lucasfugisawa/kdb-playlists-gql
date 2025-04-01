package com.fugisawa.playlistsgql.domain.services

import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import java.util.UUID

class UserService(private val userRepository: UserRepository) {
    suspend fun getById(id: UUID): User? {
        return userRepository.getById(id)
    }

    suspend fun getAll(): List<User> {
        return userRepository.getAll()
    }

    suspend fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    suspend fun create(user: User): User {
        return userRepository.create(user)
    }

    suspend fun update(user: User): User {
        return userRepository.update(user)
    }

    suspend fun delete(id: UUID): Boolean {
        return userRepository.delete(id)
    }
}