package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.entities.Genre
import com.fugisawa.playlistsgql.infrastructure.graphql.types.GraphQLCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.PageInfo
import com.fugisawa.playlistsgql.infrastructure.graphql.types.pagedCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import com.fugisawa.playlistsgql.services.SongService
import java.util.UUID
import com.fugisawa.playlistsgql.infrastructure.graphql.types.Song as SongGQL

data class SongCollection(
    override val items: List<SongGQL>,
    override val totalCount: Int,
    override val pageInfo: PageInfo,
) : GraphQLCollection<SongGQL>

class SongQueryService(
    private val songService: SongService,
) : Query {
    suspend fun song(id: UUID): SongGQL? = songService.getById(id)?.toSchemaType()

    suspend fun songs(
        filter: SongFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): SongCollection {
        val allSongs = songService.getAll()

        val filteredSongs =
            allSongs.filter { song ->
                (filter?.ids == null || filter.ids.contains(song.id)) &&
                    (filter?.title == null || song.title.contains(filter.title, ignoreCase = true)) &&
                    (filter?.artist == null || song.artist.contains(filter.artist, ignoreCase = true)) &&
                    (filter?.genre == null || song.genre == filter.genre)
            }

        val (items, totalCount, pageInfo) =
            pagedCollection(
                allItems = allSongs,
                filteredItems = filteredSongs,
                offset = offset,
                limit = limit,
                transform = { it.toSchemaType() },
            )

        return SongCollection(
            items = items,
            totalCount = totalCount,
            pageInfo = pageInfo,
        )
    }
}

data class SongFilter(
    val ids: List<UUID>? = null,
    val title: String? = null,
    val artist: String? = null,
    val genre: Genre? = null,
)
