package com.fugisawa.playlistsgql.domain.repositories

import com.fugisawa.playlistsgql.domain.entities.User
import java.util.UUID

interface UserRepository {
    suspend fun getById(id: UUID): User?

    suspend fun getAll(): List<User>

    suspend fun findByUsername(username: String): User?

    suspend fun create(user: User): User

    suspend fun update(user: User): User

    suspend fun delete(id: UUID): Boolean
}
