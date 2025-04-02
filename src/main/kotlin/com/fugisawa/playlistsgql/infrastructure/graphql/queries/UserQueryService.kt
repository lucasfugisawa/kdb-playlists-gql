package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.UserFilter
import java.util.UUID

class UserQueryService(
    private val userService: UserService,
) : Query {
    suspend fun user(id: UUID): User? {
        return userService.getById(id)
    }

    suspend fun users(filter: UserFilter? = null, limit: Int? = null, offset: Int? = null): List<User> {
        val allUsers = userService.getAll()

        val filteredUsers = allUsers.filter { user ->
            (filter?.ids == null || filter.ids.contains(user.id)) &&
            (filter?.username == null || user.username.contains(filter.username, ignoreCase = true)) &&
            true
        }

        return filteredUsers
            .let { if (offset != null) it.drop(offset) else it }
            .let { if (limit != null) it.take(limit) else it }
    }
}
