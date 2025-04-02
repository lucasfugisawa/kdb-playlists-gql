package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.Vote
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.domain.services.VoteService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.VoteCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.VoteUpdateInput
import java.util.UUID

class VoteMutationService(
    private val voteService: VoteService,
    private val playlistSongService: PlaylistSongService,
    private val userService: UserService,
) : Mutation {
    suspend fun createVote(input: VoteCreateInput): Vote? {
        val playlistSong = playlistSongService.getById(input.playlistSongId) ?: return null
        val user = userService.getById(input.userId) ?: return null

        val existingVote = voteService.findByPlaylistSongAndUser(playlistSong, user)
        if (existingVote != null) {
            if (existingVote.type == input.type) {
                return existingVote
            }
            val updatedVote = existingVote.copy(type = input.type)
            return voteService.update(updatedVote)
        }

        val vote = Vote(
            playlistSong = playlistSong,
            user = user,
            type = input.type,
        )

        return voteService.create(vote)
    }

    suspend fun updateVote(
        id: UUID,
        input: VoteUpdateInput,
    ): Vote? {
        val existingVote = voteService.getById(id) ?: return null
        val updatedVote = existingVote.copy(
            type = when (val type = input.type) {
                is OptionalInput.Defined -> type.value ?: existingVote.type
                else -> existingVote.type
            }
        )
        return voteService.update(updatedVote)
    }

    suspend fun deleteVote(id: UUID): Boolean {
        return voteService.delete(id)
    }
}
