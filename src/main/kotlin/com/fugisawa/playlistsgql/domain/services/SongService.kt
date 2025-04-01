package com.fugisawa.playlistsgql.domain.services

import com.fugisawa.playlistsgql.data.models.enums.Genre
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.repositories.SongRepository
import java.util.UUID

class SongService(private val songRepository: SongRepository) {
    suspend fun getById(id: UUID): Song? {
        return songRepository.getById(id)
    }

    suspend fun getAll(): List<Song> {
        return songRepository.getAll()
    }

    suspend fun findByTitle(title: String): List<Song> {
        return songRepository.findByTitle(title)
    }

    suspend fun findByArtist(artist: String): List<Song> {
        return songRepository.findByArtist(artist)
    }

    suspend fun findByGenre(genre: Genre): List<Song> {
        return songRepository.findByGenre(genre)
    }

    suspend fun create(song: Song): Song {
        return songRepository.create(song)
    }

    suspend fun update(song: Song): Song {
        return songRepository.update(song)
    }

    suspend fun delete(id: UUID): Boolean {
        return songRepository.delete(id)
    }
}