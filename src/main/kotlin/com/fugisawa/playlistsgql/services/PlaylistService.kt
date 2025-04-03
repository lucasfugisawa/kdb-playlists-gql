package com.fugisawa.playlistsgql.services

import com.fugisawa.playlistsgql.domain.entities.Playlist
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistRepository
import java.util.UUID

class PlaylistService(
    private val playlistRepository: PlaylistRepository,
) {
    suspend fun getById(id: UUID): Playlist? = playlistRepository.getById(id)

    suspend fun getAll(): List<Playlist> = playlistRepository.getAll()

    suspend fun findByTitle(title: String): List<Playlist> = playlistRepository.findByTitle(title)

    suspend fun findByCreator(creator: User): List<Playlist> = playlistRepository.findByCreator(creator)

    suspend fun findByTag(tag: String): List<Playlist> = playlistRepository.findByTag(tag)

    suspend fun create(playlist: Playlist): Playlist = playlistRepository.create(playlist)

    suspend fun update(playlist: Playlist): Playlist = playlistRepository.update(playlist)

    suspend fun delete(id: UUID): Boolean = playlistRepository.delete(id)
}
