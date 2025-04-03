package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.UserCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.UserUpdateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID

class UserMutationService(
    private val userService: UserService,
) : Mutation {
    suspend fun createUser(input: UserCreateInput): com.fugisawa.playlistsgql.infrastructure.graphql.types.User {
        val user =
            User(
                username = input.username,
            )
        val createdUser = userService.create(user)
        return createdUser.toSchemaType()
    }

    suspend fun updateUser(
        id: UUID,
        input: UserUpdateInput,
    ): com.fugisawa.playlistsgql.infrastructure.graphql.types.User? {
        val existingUser = userService.getById(id) ?: return null
        val updatedUser =
            existingUser.copy(
                username =
                    when (val username = input.username) {
                        is OptionalInput.Defined -> username.value ?: existingUser.username
                        else -> existingUser.username
                    },
            )
        val updatedUserResult = userService.update(updatedUser)
        return updatedUserResult?.toSchemaType()
    }

    suspend fun deleteUser(id: UUID): Boolean = userService.delete(id)
}
