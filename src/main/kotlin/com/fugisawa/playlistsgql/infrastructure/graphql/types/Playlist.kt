package com.fugisawa.playlistsgql.infrastructure.graphql.types

import com.fugisawa.playlistsgql.domain.entities.Playlist
import java.time.Instant
import java.util.*

data class Playlist(
    val id: UUID,
    val title: String,
    val description: String?,
    val creator: User,
    val createdAt: Instant,
    val tags: List<String>,
)

fun Playlist.toSchemaType(): com.fugisawa.playlistsgql.infrastructure.graphql.types.Playlist =
    Playlist(
        id = id,
        title = title,
        description = description,
        creator = creator.toSchemaType(),
        createdAt = createdAt,
        tags = tags,
    )

fun com.fugisawa.playlistsgql.infrastructure.graphql.types.Playlist.toEntity(): Playlist =
    Playlist(
        id = id,
        title = title,
        description = description,
        creator = creator.toEntity(),
        createdAt = createdAt,
        tags = tags,
    )
