package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.UserCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.UserUpdateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import com.fugisawa.playlistsgql.services.UserService
import java.util.*

class UserMutationService(
    private val userService: UserService,
) : Mutation {
    suspend fun createUser(input: UserCreateInput): com.fugisawa.playlistsgql.infrastructure.graphql.types.User {
        val createdUser =
            userService.createWithPassword(
                username = input.username,
                password = input.password,
            )
        return createdUser.toSchemaType()
    }

    suspend fun updateUser(
        id: UUID,
        input: UserUpdateInput,
    ): com.fugisawa.playlistsgql.infrastructure.graphql.types.User? {
        val existingUser = userService.getById(id) ?: return null

        val password =
            when (val passwordInput = input.password) {
                is OptionalInput.Defined -> passwordInput.value
                else -> null
            }

        val username =
            when (val usernameInput = input.username) {
                is OptionalInput.Defined -> usernameInput.value ?: existingUser.username
                else -> existingUser.username
            }

        val userToUpdate = existingUser.copy(username = username)
        val updatedUserResult = userService.updateWithPassword(userToUpdate, password)
        return updatedUserResult.toSchemaType()
    }

    suspend fun deleteUser(id: UUID): Boolean = userService.delete(id)
}
