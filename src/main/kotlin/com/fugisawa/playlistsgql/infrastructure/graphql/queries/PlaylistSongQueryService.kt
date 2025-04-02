package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistSongFilter
import java.util.UUID

class PlaylistSongQueryService(
    private val playlistSongService: PlaylistSongService,
    private val playlistService: PlaylistService,
    private val songService: SongService,
    private val userService: UserService,
) : Query {
    suspend fun playlistSong(id: UUID): PlaylistSong? = playlistSongService.getById(id)

    suspend fun playlistSongs(
        filter: PlaylistSongFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): List<PlaylistSong> {
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
    }
}
