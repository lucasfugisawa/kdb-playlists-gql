package com.fugisawa.playlistsgql.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.User
import java.util.UUID

data class AuthPayload(
    val token: String? = null,
    val user: User? = null,
)

class LoginMutationService : Mutation {
    suspend fun login(username: String): AuthPayload {
        val token = "fake-token"
        val user =
            User(
                id = UUID.randomUUID(),
                username = username,
            )
        return AuthPayload(token, user)
    }
}
