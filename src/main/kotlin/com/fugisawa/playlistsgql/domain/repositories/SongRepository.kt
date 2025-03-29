package com.fugisawa.playlistsgql.domain.repositories

import com.fugisawa.playlistsgql.data.models.enums.Genre
import com.fugisawa.playlistsgql.domain.models.Song
import java.util.UUID

interface SongRepository {
    suspend fun getById(id: UUID): Song?

    suspend fun getAll(): List<Song>

    suspend fun findByTitle(title: String): List<Song>

    suspend fun findByArtist(artist: String): List<Song>

    suspend fun findByGenre(genre: Genre): List<Song>

    suspend fun create(song: Song): Song

    suspend fun update(song: Song): Song

    suspend fun delete(id: UUID): Boolean
}
