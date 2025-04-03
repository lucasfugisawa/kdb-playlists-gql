package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID
import com.fugisawa.playlistsgql.infrastructure.graphql.types.GraphQLCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.PageInfo
import com.fugisawa.playlistsgql.infrastructure.graphql.types.pagedCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong as PlaylistSongGQL

data class PlaylistSongCollection(
    override val items: List<PlaylistSongGQL>,
    override val totalCount: Int,
    override val pageInfo: PageInfo
) : GraphQLCollection<PlaylistSongGQL>

class PlaylistSongQueryService(
    private val playlistSongService: PlaylistSongService,
) : Query {
    suspend fun playlistSong(id: UUID): PlaylistSongGQL? =
        playlistSongService.getById(id)?.toSchemaType()

    suspend fun playlistSongs(
        filter: PlaylistSongFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): PlaylistSongCollection {
        val allPlaylistSongs = playlistSongService.getAll()

        val filteredPlaylistSongs =
            allPlaylistSongs.filter { playlistSong ->
                (filter?.ids == null || filter.ids.contains(playlistSong.id)) &&
                    (filter?.playlistId == null || playlistSong.playlist.id == filter.playlistId) &&
                    (filter?.songId == null || playlistSong.song.id == filter.songId) &&
                    (filter?.addedById == null || playlistSong.addedBy.id == filter.addedById) &&
                    (filter?.position == null || playlistSong.position == filter.position)
            }

        val (items, totalCount, pageInfo) = pagedCollection(
            allItems = allPlaylistSongs,
            filteredItems = filteredPlaylistSongs,
            offset = offset,
            limit = limit,
            transform = { it.toSchemaType() }
        )

        return PlaylistSongCollection(
            items = items,
            totalCount = totalCount,
            pageInfo = pageInfo
        )
    }
}

data class PlaylistSongFilter(
    val ids: List<UUID>? = null,
    val playlistId: UUID? = null,
    val songId: UUID? = null,
    val addedById: UUID? = null,
    val position: Int? = null,
)
