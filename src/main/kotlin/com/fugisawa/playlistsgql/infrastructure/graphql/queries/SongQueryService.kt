package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.data.models.enums.Genre
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.services.SongService
import java.util.UUID

class SongQueryService(
    private val songService: SongService,
) : Query {
    suspend fun song(id: UUID): Song? {
        return songService.getById(id)
    }

    suspend fun songs(): List<Song> {
        return songService.getAll()
    }

    suspend fun songsByTitle(title: String): List<Song> {
        return songService.findByTitle(title)
    }

    suspend fun songsByArtist(artist: String): List<Song> {
        return songService.findByArtist(artist)
    }

    suspend fun songsByGenre(genre: Genre): List<Song> {
        return songService.findByGenre(genre)
    }
}