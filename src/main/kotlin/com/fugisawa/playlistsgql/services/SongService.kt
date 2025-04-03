package com.fugisawa.playlistsgql.services

import com.fugisawa.playlistsgql.domain.entities.Genre
import com.fugisawa.playlistsgql.domain.entities.Song
import com.fugisawa.playlistsgql.domain.repositories.SongRepository
import java.util.UUID

class SongService(
    private val songRepository: SongRepository,
) {
    suspend fun getById(id: UUID): Song? = songRepository.getById(id)

    suspend fun getByIds(ids: List<UUID>): List<Song> = songRepository.getByIds(ids)

    suspend fun getAll(): List<Song> = songRepository.getAll()

    suspend fun findByTitle(title: String): List<Song> = songRepository.findByTitle(title)

    suspend fun findByArtist(artist: String): List<Song> = songRepository.findByArtist(artist)

    suspend fun findByGenre(genre: Genre): List<Song> = songRepository.findByGenre(genre)

    suspend fun create(song: Song): Song = songRepository.create(song)

    suspend fun update(song: Song): Song = songRepository.update(song)

    suspend fun delete(id: UUID): Boolean = songRepository.delete(id)
}
