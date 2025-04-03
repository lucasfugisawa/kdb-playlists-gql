package com.fugisawa.playlistsgql.infrastructure.graphql.types

import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import java.util.UUID

data class PlaylistSong(
    val id: UUID,
    val playlist: Playlist,
    val song: Song,
    val addedBy: User,
    val position: Int,
)

fun PlaylistSong.toSchemaType(): com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong =
    PlaylistSong(
        id = id,
        playlist = playlist.toSchemaType(),
        song = song.toSchemaType(),
        addedBy = addedBy.toSchemaType(),
        position = position,
    )

fun com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong.toEntity(): PlaylistSong =
    PlaylistSong(
        id = id,
        playlist = playlist.toEntity(),
        song = song.toEntity(),
        addedBy = addedBy.toEntity(),
        position = position,
    )
