package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.services.UserService
import java.util.UUID

class UserQueryService(
    private val userService: UserService,
) : Query {
    suspend fun user(id: UUID): User? {
        return userService.getById(id)
    }

    suspend fun users(): List<User> {
        return userService.getAll()
    }

    suspend fun userByUsername(username: String): User? {
        return userService.findByUsername(username)
    }
}