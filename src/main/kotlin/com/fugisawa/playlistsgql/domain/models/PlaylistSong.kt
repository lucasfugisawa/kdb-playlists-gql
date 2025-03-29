package com.fugisawa.playlistsgql.domain.models

import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.models.User
import java.util.UUID

data class PlaylistSong(
    val id: UUID = UUID.randomUUID(),
    val playlist: Playlist,
    val song: Song,
    val addedBy: User,
    val position: Int,
)
