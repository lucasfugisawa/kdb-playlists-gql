package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.types.GraphQLCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.PageInfo
import com.fugisawa.playlistsgql.infrastructure.graphql.types.pagedCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID
import com.fugisawa.playlistsgql.infrastructure.graphql.types.User as UserGQL

data class UserCollection(
    override val items: List<UserGQL>,
    override val totalCount: Int,
    override val pageInfo: PageInfo,
) : GraphQLCollection<UserGQL>

class UserQueryService(
    private val userService: UserService,
) : Query {
    suspend fun user(id: UUID): UserGQL? = userService.getById(id)?.toSchemaType()

    suspend fun users(
        filter: UserFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): UserCollection {
        val allUsers = userService.getAll()

        val filteredUsers =
            allUsers.filter { user ->
                (filter?.ids == null || filter.ids.contains(user.id)) &&
                    (filter?.username == null || user.username.contains(filter.username, ignoreCase = true)) &&
                    true
            }

        val (items, totalCount, pageInfo) =
            pagedCollection(
                allItems = allUsers,
                filteredItems = filteredUsers,
                offset = offset,
                limit = limit,
                transform = { it.toSchemaType() },
            )

        return UserCollection(
            items = items,
            totalCount = totalCount,
            pageInfo = pageInfo,
        )
    }
}

data class UserFilter(
    val ids: List<UUID>? = null,
    val username: String? = null,
    val email: String? = null,
)
