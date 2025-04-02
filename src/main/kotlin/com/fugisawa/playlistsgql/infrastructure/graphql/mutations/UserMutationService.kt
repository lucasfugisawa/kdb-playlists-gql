package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.UserCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.UserUpdateInput
import java.util.UUID

class UserMutationService(
    private val userService: UserService,
) : Mutation {
    suspend fun createUser(input: UserCreateInput): User {
        val user =
            User(
                username = input.username,
            )
        return userService.create(user)
    }

    suspend fun updateUser(
        id: UUID,
        input: UserUpdateInput,
    ): User? {
        val existingUser = userService.getById(id) ?: return null
        val updatedUser =
            existingUser.copy(
                username =
                    when (val username = input.username) {
                        is OptionalInput.Defined -> username.value ?: existingUser.username
                        else -> existingUser.username
                    },
            )
        return userService.update(updatedUser)
    }

    suspend fun deleteUser(id: UUID): Boolean = userService.delete(id)
}
