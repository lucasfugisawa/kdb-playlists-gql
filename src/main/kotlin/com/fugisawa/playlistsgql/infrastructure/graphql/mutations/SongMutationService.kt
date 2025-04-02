package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.SongCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.SongUpdateInput
import java.util.UUID

class SongMutationService(
    private val songService: SongService,
) : Mutation {
    suspend fun createSong(input: SongCreateInput): Song {
        val song =
            Song(
                title = input.title,
                artist = input.artist,
                duration = input.duration,
                genre = input.genre,
            )
        return songService.create(song)
    }

    suspend fun updateSong(
        id: UUID,
        input: SongUpdateInput,
    ): Song? {
        val existingSong = songService.getById(id) ?: return null
        val updatedSong =
            existingSong.copy(
                title =
                    when (val title = input.title) {
                        is OptionalInput.Defined -> title.value ?: existingSong.title
                        else -> existingSong.title
                    },
                artist =
                    when (val artist = input.artist) {
                        is OptionalInput.Defined -> artist.value ?: existingSong.artist
                        else -> existingSong.artist
                    },
                duration =
                    when (val duration = input.duration) {
                        is OptionalInput.Defined -> duration.value ?: existingSong.duration
                        else -> existingSong.duration
                    },
                genre =
                    when (val genre = input.genre) {
                        is OptionalInput.Defined -> genre.value ?: existingSong.genre
                        else -> existingSong.genre
                    },
            )
        return songService.update(updatedSong)
    }

    suspend fun deleteSong(id: UUID): Boolean = songService.delete(id)
}
