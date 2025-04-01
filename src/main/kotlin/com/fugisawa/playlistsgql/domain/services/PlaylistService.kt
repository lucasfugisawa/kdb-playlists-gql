package com.fugisawa.playlistsgql.domain.services

import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistRepository
import java.util.UUID

class PlaylistService(private val playlistRepository: PlaylistRepository) {
    suspend fun getById(id: UUID): Playlist? {
        return playlistRepository.getById(id)
    }

    suspend fun getAll(): List<Playlist> {
        return playlistRepository.getAll()
    }

    suspend fun findByTitle(title: String): List<Playlist> {
        return playlistRepository.findByTitle(title)
    }

    suspend fun findByCreator(creator: User): List<Playlist> {
        return playlistRepository.findByCreator(creator)
    }

    suspend fun findByTag(tag: String): List<Playlist> {
        return playlistRepository.findByTag(tag)
    }

    suspend fun create(playlist: Playlist): Playlist {
        return playlistRepository.create(playlist)
    }

    suspend fun update(playlist: Playlist): Playlist {
        return playlistRepository.update(playlist)
    }

    suspend fun delete(id: UUID): Boolean {
        return playlistRepository.delete(id)
    }
}