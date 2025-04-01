package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.UserService
import java.util.UUID

class PlaylistQueryService(
    private val playlistService: PlaylistService,
    private val userService: UserService,
) : Query {
    suspend fun playlist(id: UUID): Playlist? {
        return playlistService.getById(id)
    }

    suspend fun playlists(): List<Playlist> {
        return playlistService.getAll()
    }

    suspend fun playlistsByTitle(title: String): List<Playlist> {
        return playlistService.findByTitle(title)
    }

    suspend fun playlistsByCreator(creatorId: UUID): List<Playlist> {
        val creator = userService.getById(creatorId) ?: return emptyList()
        return playlistService.findByCreator(creator)
    }

    suspend fun playlistsByTag(tag: String): List<Playlist> {
        return playlistService.findByTag(tag)
    }
}