package com.fugisawa.playlistsgql.domain.services

import com.fugisawa.playlistsgql.domain.entities.Playlist
import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.domain.entities.Song
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistSongRepository
import java.util.UUID

class PlaylistSongService(
    private val playlistSongRepository: PlaylistSongRepository,
) {
    suspend fun getById(id: UUID): PlaylistSong? = playlistSongRepository.getById(id)

    suspend fun getAll(): List<PlaylistSong> = playlistSongRepository.getAll()

    suspend fun findByPlaylist(playlist: Playlist): List<PlaylistSong> = playlistSongRepository.findByPlaylist(playlist)

    suspend fun findBySong(song: Song): List<PlaylistSong> = playlistSongRepository.findBySong(song)

    suspend fun findByAddedBy(user: User): List<PlaylistSong> = playlistSongRepository.findByAddedBy(user)

    suspend fun findByPlaylistAndPosition(
        playlist: Playlist,
        position: Int,
    ): PlaylistSong? = playlistSongRepository.findByPlaylistAndPosition(playlist, position)

    suspend fun create(playlistSong: PlaylistSong): PlaylistSong = playlistSongRepository.create(playlistSong)

    suspend fun update(playlistSong: PlaylistSong): PlaylistSong = playlistSongRepository.update(playlistSong)

    suspend fun delete(id: UUID): Boolean = playlistSongRepository.delete(id)
}
