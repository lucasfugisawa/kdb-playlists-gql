package com.fugisawa.playlistsgql.domain.services

import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistSongRepository
import java.util.UUID

class PlaylistSongService(private val playlistSongRepository: PlaylistSongRepository) {
    suspend fun getById(id: UUID): PlaylistSong? {
        return playlistSongRepository.getById(id)
    }

    suspend fun getAll(): List<PlaylistSong> {
        return playlistSongRepository.getAll()
    }

    suspend fun findByPlaylist(playlist: Playlist): List<PlaylistSong> {
        return playlistSongRepository.findByPlaylist(playlist)
    }

    suspend fun findBySong(song: Song): List<PlaylistSong> {
        return playlistSongRepository.findBySong(song)
    }

    suspend fun findByAddedBy(user: User): List<PlaylistSong> {
        return playlistSongRepository.findByAddedBy(user)
    }

    suspend fun findByPlaylistAndPosition(
        playlist: Playlist,
        position: Int,
    ): PlaylistSong? {
        return playlistSongRepository.findByPlaylistAndPosition(playlist, position)
    }

    suspend fun create(playlistSong: PlaylistSong): PlaylistSong {
        return playlistSongRepository.create(playlistSong)
    }

    suspend fun update(playlistSong: PlaylistSong): PlaylistSong {
        return playlistSongRepository.update(playlistSong)
    }

    suspend fun delete(id: UUID): Boolean {
        return playlistSongRepository.delete(id)
    }
}