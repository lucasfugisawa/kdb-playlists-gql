package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID

class UserQueryService(
    private val userService: UserService,
) : Query {
    suspend fun user(id: UUID): com.fugisawa.playlistsgql.infrastructure.graphql.types.User? = userService.getById(id)?.toSchemaType()

    suspend fun users(
        filter: UserFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): List<com.fugisawa.playlistsgql.infrastructure.graphql.types.User> {
        val allUsers = userService.getAll()

        val filteredUsers =
            allUsers.filter { user ->
                (filter?.ids == null || filter.ids.contains(user.id)) &&
                    (filter?.username == null || user.username.contains(filter.username, ignoreCase = true)) &&
                    true
            }

        return filteredUsers
            .let { if (offset != null) it.drop(offset) else it }
            .let { if (limit != null) it.take(limit) else it }
            .map { it.toSchemaType() }
    }
}

data class UserFilter(
    val ids: List<UUID>? = null,
    val username: String? = null,
    val email: String? = null,
)
