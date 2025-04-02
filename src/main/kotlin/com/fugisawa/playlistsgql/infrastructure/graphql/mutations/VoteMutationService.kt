package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.data.models.enums.VoteType
import com.fugisawa.playlistsgql.domain.models.Vote
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.domain.services.VoteService
import java.util.UUID

class VoteMutationService(
    private val voteService: VoteService,
    private val playlistSongService: PlaylistSongService,
    private val userService: UserService,
) : Mutation {
    suspend fun createVote(
        playlistSongId: UUID,
        userId: UUID,
        type: VoteType,
    ): Vote? {
        val playlistSong = playlistSongService.getById(playlistSongId) ?: return null
        val user = userService.getById(userId) ?: return null
        
        val existingVote = voteService.findByPlaylistSongAndUser(playlistSong, user)
        if (existingVote != null) {
            if (existingVote.type == type) {
                return existingVote
            }
            val updatedVote = existingVote.copy(type = type)
            return voteService.update(updatedVote)
        }
        
        val vote = Vote(
            playlistSong = playlistSong,
            user = user,
            type = type,
        )
        
        return voteService.create(vote)
    }
    
    suspend fun updateVote(
        id: UUID,
        type: VoteType,
    ): Vote? {
        val existingVote = voteService.getById(id) ?: return null
        val updatedVote = existingVote.copy(type = type)
        return voteService.update(updatedVote)
    }
    
    suspend fun deleteVote(id: UUID): Boolean {
        return voteService.delete(id)
    }
}