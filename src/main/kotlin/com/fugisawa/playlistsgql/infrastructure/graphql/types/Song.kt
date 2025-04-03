package com.fugisawa.playlistsgql.infrastructure.graphql.types

import com.fugisawa.playlistsgql.data.models.enums.Genre
import com.fugisawa.playlistsgql.domain.models.Song
import java.util.UUID

data class Song(
    val id: UUID,
    val title: String,
    val artist: String,
    val duration: Int,
    val genre: Genre,
)

fun Song.toSchemaType(): com.fugisawa.playlistsgql.infrastructure.graphql.types.Song =
    com.fugisawa.playlistsgql.infrastructure.graphql.types.Song(
        id = id,
        title = title,
        artist = artist,
        duration = duration,
        genre = genre,
    )

fun com.fugisawa.playlistsgql.infrastructure.graphql.types.Song.toEntity(): Song =
    Song(
        id = id,
        title = title,
        artist = artist,
        duration = duration,
        genre = genre,
    )
