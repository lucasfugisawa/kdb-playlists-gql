package com.fugisawa.playlistsgql.infrastructure.graphql.types

import com.fugisawa.playlistsgql.domain.models.User
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
)

fun User.toSchemaType(): com.fugisawa.playlistsgql.infrastructure.graphql.types.User =
    com.fugisawa.playlistsgql.infrastructure.graphql.types.User(
        id = id,
        username = username,
    )

fun com.fugisawa.playlistsgql.infrastructure.graphql.types.User.toEntity(): User =
    User(
        id = id,
        username = username,
    )
