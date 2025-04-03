package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID

class PlaylistSongQueryService(
    private val playlistSongService: PlaylistSongService,
) : Query {
    suspend fun playlistSong(id: UUID): com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong? =
        playlistSongService.getById(id)?.toSchemaType()

    suspend fun playlistSongs(
        filter: PlaylistSongFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): List<com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong> {
        val allPlaylistSongs = playlistSongService.getAll()

        val filteredPlaylistSongs =
            allPlaylistSongs.filter { playlistSong ->
                (filter?.ids == null || filter.ids.contains(playlistSong.id)) &&
                    (filter?.playlistId == null || playlistSong.playlist.id == filter.playlistId) &&
                    (filter?.songId == null || playlistSong.song.id == filter.songId) &&
                    (filter?.addedById == null || playlistSong.addedBy.id == filter.addedById) &&
                    (filter?.position == null || playlistSong.position == filter.position)
            }

        return filteredPlaylistSongs
            .let { if (offset != null) it.drop(offset) else it }
            .let { if (limit != null) it.take(limit) else it }
            .map { it.toSchemaType() }
    }
}

data class PlaylistSongFilter(
    val ids: List<UUID>? = null,
    val playlistId: UUID? = null,
    val songId: UUID? = null,
    val addedById: UUID? = null,
    val position: Int? = null,
)
