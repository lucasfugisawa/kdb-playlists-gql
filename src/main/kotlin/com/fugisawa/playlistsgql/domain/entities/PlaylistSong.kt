package com.fugisawa.playlistsgql.domain.entities

import java.util.UUID

data class PlaylistSong(
    val id: UUID = UUID.randomUUID(),
    val playlist: Playlist,
    val song: Song,
    val addedBy: User,
    val position: Int,
)
