package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import java.util.UUID

class PlaylistSongQueryService(
    private val playlistSongService: PlaylistSongService,
    private val playlistService: PlaylistService,
    private val songService: SongService,
    private val userService: UserService,
) : Query {
    suspend fun playlistSong(id: UUID): PlaylistSong? {
        return playlistSongService.getById(id)
    }

    suspend fun playlistSongs(): List<PlaylistSong> {
        return playlistSongService.getAll()
    }

    suspend fun playlistSongsByPlaylist(playlistId: UUID): List<PlaylistSong> {
        val playlist = playlistService.getById(playlistId) ?: return emptyList()
        return playlistSongService.findByPlaylist(playlist)
    }

    suspend fun playlistSongsBySong(songId: UUID): List<PlaylistSong> {
        val song = songService.getById(songId) ?: return emptyList()
        return playlistSongService.findBySong(song)
    }

    suspend fun playlistSongsByAddedBy(userId: UUID): List<PlaylistSong> {
        val user = userService.getById(userId) ?: return emptyList()
        return playlistSongService.findByAddedBy(user)
    }

    suspend fun playlistSongByPlaylistAndPosition(playlistId: UUID, position: Int): PlaylistSong? {
        val playlist = playlistService.getById(playlistId) ?: return null
        return playlistSongService.findByPlaylistAndPosition(playlist, position)
    }
}