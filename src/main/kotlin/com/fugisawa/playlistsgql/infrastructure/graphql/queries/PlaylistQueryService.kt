package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.infrastructure.graphql.types.GraphQLCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.PageInfo
import com.fugisawa.playlistsgql.infrastructure.graphql.types.pagedCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import com.fugisawa.playlistsgql.services.PlaylistService
import java.time.Instant
import java.util.UUID
import com.fugisawa.playlistsgql.infrastructure.graphql.types.Playlist as PlaylistGQL

data class PlaylistCollection(
    override val items: List<PlaylistGQL>,
    override val totalCount: Int,
    override val pageInfo: PageInfo,
) : GraphQLCollection<PlaylistGQL>

class PlaylistQueryService(
    private val playlistService: PlaylistService,
) : Query {
    suspend fun playlist(id: UUID): PlaylistGQL? = playlistService.getById(id)?.toSchemaType()

    suspend fun playlists(
        filter: PlaylistFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): PlaylistCollection {
        val allPlaylists = playlistService.getAll()

        val filteredPlaylists =
            allPlaylists.filter { playlist ->
                (filter?.ids == null || filter.ids.contains(playlist.id)) &&
                    (filter?.creatorId == null || playlist.creator.id == filter.creatorId) &&
                    (filter?.tag == null || playlist.tags.contains(filter.tag)) &&
                    (filter?.title == null || playlist.title.contains(filter.title, ignoreCase = true)) &&
                    (filter?.createdBefore == null || playlist.createdAt.isBefore(filter.createdBefore)) &&
                    (filter?.createdAfter == null || playlist.createdAt.isAfter(filter.createdAfter))
            }

        val (items, totalCount, pageInfo) =
            pagedCollection(
                allItems = allPlaylists,
                filteredItems = filteredPlaylists,
                offset = offset,
                limit = limit,
                transform = { it.toSchemaType() },
            )

        return PlaylistCollection(
            items = items,
            totalCount = totalCount,
            pageInfo = pageInfo,
        )
    }
}

data class PlaylistFilter(
    val ids: List<UUID>? = null,
    val creatorId: UUID? = null,
    val tag: String? = null,
    val title: String? = null,
    val createdBefore: Instant? = null,
    val createdAfter: Instant? = null,
)
