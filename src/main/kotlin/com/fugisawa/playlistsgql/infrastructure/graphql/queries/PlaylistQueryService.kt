package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistFilter
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID

class PlaylistQueryService(
    private val playlistService: PlaylistService,
    private val userService: UserService,
) : Query {
    suspend fun playlist(id: UUID): com.fugisawa.playlistsgql.infrastructure.graphql.types.Playlist? = playlistService.getById(id)?.toSchemaType()

    suspend fun playlists(
        filter: PlaylistFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): List<com.fugisawa.playlistsgql.infrastructure.graphql.types.Playlist> {
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

        return filteredPlaylists
            .let { if (offset != null) it.drop(offset) else it }
            .let { if (limit != null) it.take(limit) else it }
            .map { it.toSchemaType() }
    }
}
