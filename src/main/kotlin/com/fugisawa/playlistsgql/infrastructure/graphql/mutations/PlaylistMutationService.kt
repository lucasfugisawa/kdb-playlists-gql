package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.UserService
import java.util.UUID

class PlaylistMutationService(
    private val playlistService: PlaylistService,
    private val userService: UserService,
) : Mutation {
    suspend fun createPlaylist(
        title: String,
        description: String?,
        creatorId: UUID,
        tags: List<String>?,
    ): Playlist? {
        val creator = userService.getById(creatorId) ?: return null
        val playlist = Playlist(
            title = title,
            description = description,
            creator = creator,
            tags = tags ?: emptyList(),
        )
        return playlistService.create(playlist)
    }

    suspend fun updatePlaylist(
        id: UUID,
        title: String?,
        description: String?,
        tags: List<String>?,
    ): Playlist? {
        val existingPlaylist = playlistService.getById(id) ?: return null
        val updatedPlaylist = existingPlaylist.copy(
            title = title ?: existingPlaylist.title,
            description = description ?: existingPlaylist.description,
            tags = tags ?: existingPlaylist.tags,
        )
        return playlistService.update(updatedPlaylist)
    }

    suspend fun deletePlaylist(id: UUID): Boolean {
        return playlistService.delete(id)
    }
}