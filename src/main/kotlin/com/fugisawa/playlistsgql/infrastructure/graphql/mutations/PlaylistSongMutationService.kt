package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistSongCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistSongUpdateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID

class PlaylistSongMutationService(
    private val playlistSongService: PlaylistSongService,
    private val playlistService: PlaylistService,
    private val songService: SongService,
    private val userService: UserService,
) : Mutation {
    suspend fun addSongToPlaylist(input: PlaylistSongCreateInput): com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong? {
        val playlist = playlistService.getById(input.playlistId) ?: return null
        val song = songService.getById(input.songId) ?: return null
        val addedBy = userService.getById(input.addedById) ?: return null

        val playlistSong =
            PlaylistSong(
                playlist = playlist,
                song = song,
                addedBy = addedBy,
                position = input.position,
            )

        val createdPlaylistSong = playlistSongService.create(playlistSong)
        return createdPlaylistSong.toSchemaType()
    }

    suspend fun updatePlaylistSongPosition(
        id: UUID,
        input: PlaylistSongUpdateInput,
    ): com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong? {
        val existingPlaylistSong = playlistSongService.getById(id) ?: return null
        val updatedPlaylistSong =
            existingPlaylistSong.copy(
                position =
                    when (val position = input.position) {
                        is OptionalInput.Defined -> position.value ?: existingPlaylistSong.position
                        else -> existingPlaylistSong.position
                    },
            )
        val updatedPlaylistSongResult = playlistSongService.update(updatedPlaylistSong)
        return updatedPlaylistSongResult?.toSchemaType()
    }

    suspend fun removeSongFromPlaylist(id: UUID): Boolean = playlistSongService.delete(id)
}
