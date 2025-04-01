package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.data.models.enums.Genre
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.services.SongService
import java.util.UUID

class SongMutationService(
    private val songService: SongService,
) : Mutation {
    suspend fun createSong(
        title: String,
        artist: String,
        duration: Int,
        genre: Genre,
    ): Song {
        val song = Song(
            title = title,
            artist = artist,
            duration = duration,
            genre = genre,
        )
        return songService.create(song)
    }

    suspend fun updateSong(
        id: UUID,
        title: String?,
        artist: String?,
        duration: Int?,
        genre: Genre?,
    ): Song? {
        val existingSong = songService.getById(id) ?: return null
        val updatedSong = existingSong.copy(
            title = title ?: existingSong.title,
            artist = artist ?: existingSong.artist,
            duration = duration ?: existingSong.duration,
            genre = genre ?: existingSong.genre,
        )
        return songService.update(updatedSong)
    }

    suspend fun deleteSong(id: UUID): Boolean {
        return songService.delete(id)
    }
}