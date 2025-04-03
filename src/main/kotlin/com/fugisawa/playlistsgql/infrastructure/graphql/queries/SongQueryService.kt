package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID
import com.fugisawa.playlistsgql.data.models.enums.Genre

class SongQueryService(
    private val songService: SongService,
) : Query {
    suspend fun song(id: UUID): com.fugisawa.playlistsgql.infrastructure.graphql.types.Song? = songService.getById(id)?.toSchemaType()

    suspend fun songs(
        filter: SongFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): List<com.fugisawa.playlistsgql.infrastructure.graphql.types.Song> {
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
            .map { it.toSchemaType() }
    }
}

data class SongFilter(
    val ids: List<UUID>? = null,
    val title: String? = null,
    val artist: String? = null,
    val genre: Genre? = null,
)
