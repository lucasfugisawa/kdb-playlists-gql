package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.SongFilter
import java.util.UUID

class SongQueryService(
    private val songService: SongService,
) : Query {
    suspend fun song(id: UUID): Song? = songService.getById(id)

    suspend fun songs(
        filter: SongFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): List<Song> {
        val allSongs = songService.getAll()

        val filteredSongs =
            allSongs.filter { song ->
                (filter?.ids == null || filter.ids.contains(song.id)) &&
                    (filter?.title == null || song.title.contains(filter.title, ignoreCase = true)) &&
                    (filter?.artist == null || song.artist.contains(filter.artist, ignoreCase = true)) &&
                    (filter?.genre == null || song.genre == filter.genre)
            }

        return filteredSongs
            .let { if (offset != null) it.drop(offset) else it }
            .let { if (limit != null) it.take(limit) else it }
    }
}
