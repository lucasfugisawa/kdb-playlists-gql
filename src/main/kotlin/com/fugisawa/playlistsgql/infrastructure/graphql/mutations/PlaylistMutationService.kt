package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.entities.Playlist
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.PlaylistUpdateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import com.fugisawa.playlistsgql.services.PlaylistService
import com.fugisawa.playlistsgql.services.UserService
import java.util.UUID

class PlaylistMutationService(
    private val playlistService: PlaylistService,
    private val userService: UserService,
) : Mutation {
    suspend fun createPlaylist(input: PlaylistCreateInput): com.fugisawa.playlistsgql.infrastructure.graphql.types.Playlist? {
        val creator = userService.getById(input.creatorId) ?: return null
        val playlist =
            Playlist(
                title = input.title,
                description = input.description,
                creator = creator,
                tags = input.tags ?: emptyList(),
            )
        val createdPlaylist = playlistService.create(playlist)
        return createdPlaylist.toSchemaType()
    }

    suspend fun updatePlaylist(
        id: UUID,
        input: PlaylistUpdateInput,
    ): com.fugisawa.playlistsgql.infrastructure.graphql.types.Playlist? {
        val existingPlaylist = playlistService.getById(id) ?: return null
        val updatedPlaylist =
            existingPlaylist.copy(
                title =
                    when (val title = input.title) {
                        is OptionalInput.Defined -> title.value ?: existingPlaylist.title
                        else -> existingPlaylist.title
                    },
                description =
                    when (val description = input.description) {
                        is OptionalInput.Defined -> description.value
                        else -> existingPlaylist.description
                    },
                tags =
                    when (val tags = input.tags) {
                        is OptionalInput.Defined -> tags.value ?: existingPlaylist.tags
                        else -> existingPlaylist.tags
                    },
            )
        val updatedPlaylistResult = playlistService.update(updatedPlaylist)
        return updatedPlaylistResult?.toSchemaType()
    }

    suspend fun deletePlaylist(id: UUID): Boolean = playlistService.delete(id)
}
