package com.fugisawa.playlistsgql.domain.repositories

import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.models.User
import java.util.UUID

interface PlaylistRepository {
    suspend fun getById(id: UUID): Playlist?

    suspend fun getAll(): List<Playlist>

    suspend fun findByTitle(title: String): List<Playlist>

    suspend fun findByCreator(creator: User): List<Playlist>

    suspend fun findByTag(tag: String): List<Playlist>

    suspend fun create(playlist: Playlist): Playlist

    suspend fun update(playlist: Playlist): Playlist

    suspend fun delete(id: UUID): Boolean
}
