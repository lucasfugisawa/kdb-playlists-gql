package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.data.models.enums.VoteType
import com.fugisawa.playlistsgql.domain.models.Vote
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.domain.services.VoteService
import java.util.UUID

class VoteQueryService(
    private val voteService: VoteService,
    private val playlistSongService: PlaylistSongService,
    private val userService: UserService,
) : Query {
    suspend fun vote(id: UUID): Vote? {
        return voteService.getById(id)
    }

    suspend fun votes(): List<Vote> {
        return voteService.getAll()
    }

    suspend fun votesByPlaylistSong(playlistSongId: UUID): List<Vote> {
        val playlistSong = playlistSongService.getById(playlistSongId) ?: return emptyList()
        return voteService.findByPlaylistSong(playlistSong)
    }

    suspend fun votesByUser(userId: UUID): List<Vote> {
        val user = userService.getById(userId) ?: return emptyList()
        return voteService.findByUser(user)
    }

    suspend fun voteByPlaylistSongAndUser(playlistSongId: UUID, userId: UUID): Vote? {
        val playlistSong = playlistSongService.getById(playlistSongId) ?: return null
        val user = userService.getById(userId) ?: return null
        return voteService.findByPlaylistSongAndUser(playlistSong, user)
    }

    suspend fun votesByType(type: VoteType): List<Vote> {
        return voteService.findByType(type)
    }
}