package com.fugisawa.playlistsgql.domain.repositories

import com.fugisawa.playlistsgql.domain.entities.Playlist
import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.domain.entities.Song
import com.fugisawa.playlistsgql.domain.entities.User
import java.util.UUID

interface PlaylistSongRepository {
    suspend fun getById(id: UUID): PlaylistSong?

    suspend fun getAll(): List<PlaylistSong>

    suspend fun findByPlaylist(playlist: Playlist): List<PlaylistSong>

    suspend fun findBySong(song: Song): List<PlaylistSong>

    suspend fun findByAddedBy(user: User): List<PlaylistSong>

    suspend fun findByPlaylistAndPosition(
        playlist: Playlist,
        position: Int,
    ): PlaylistSong?

    suspend fun create(playlistSong: PlaylistSong): PlaylistSong

    suspend fun update(playlistSong: PlaylistSong): PlaylistSong

    suspend fun delete(id: UUID): Boolean
}
