package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistSongCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistSongUpdateInput
import java.util.UUID

class PlaylistSongMutationService(
    private val playlistSongService: PlaylistSongService,
    private val playlistService: PlaylistService,
    private val songService: SongService,
    private val userService: UserService,
) : Mutation {
    suspend fun addSongToPlaylist(input: PlaylistSongCreateInput): PlaylistSong? {
        val playlist = playlistService.getById(input.playlistId) ?: return null
        val song = songService.getById(input.songId) ?: return null
        val addedBy = userService.getById(input.addedById) ?: return null

        val playlistSong = PlaylistSong(
            playlist = playlist,
            song = song,
            addedBy = addedBy,
            position = input.position,
        )

        return playlistSongService.create(playlistSong)
    }

    suspend fun updatePlaylistSongPosition(
        id: UUID,
        input: PlaylistSongUpdateInput,
    ): PlaylistSong? {
        val existingPlaylistSong = playlistSongService.getById(id) ?: return null
        val updatedPlaylistSong = existingPlaylistSong.copy(
            position = when (val position = input.position) {
                is OptionalInput.Defined -> position.value ?: existingPlaylistSong.position
                else -> existingPlaylistSong.position
            },
        )
        return playlistSongService.update(updatedPlaylistSong)
    }

    suspend fun removeSongFromPlaylist(id: UUID): Boolean {
        return playlistSongService.delete(id)
    }
}
