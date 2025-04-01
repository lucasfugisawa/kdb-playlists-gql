package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.services.UserService
import java.util.UUID

class UserMutationService(
    private val userService: UserService,
) : Mutation {
    suspend fun createUser(
        username: String,
    ): User {
        val user = User(
            username = username,
        )
        return userService.create(user)
    }

    suspend fun updateUser(
        id: UUID,
        username: String?,
    ): User? {
        val existingUser = userService.getById(id) ?: return null
        val updatedUser = existingUser.copy(
            username = username ?: existingUser.username,
        )
        return userService.update(updatedUser)
    }

    suspend fun deleteUser(id: UUID): Boolean {
        return userService.delete(id)
    }
}