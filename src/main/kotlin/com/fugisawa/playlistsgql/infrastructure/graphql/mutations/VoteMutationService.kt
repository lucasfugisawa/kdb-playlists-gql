package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.generator.execution.OptionalInput
import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.entities.Vote
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.domain.services.VoteService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.VoteCreateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.VoteUpdateInput
import com.fugisawa.playlistsgql.infrastructure.graphql.types.VoteGQL
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID

class VoteMutationService(
    private val voteService: VoteService,
    private val playlistSongService: PlaylistSongService,
    private val userService: UserService,
) : Mutation {
    suspend fun createVote(input: VoteCreateInput): VoteGQL? {
        val playlistSong = playlistSongService.getById(input.playlistSongId) ?: return null
        val user = userService.getById(input.userId) ?: return null

        val existingVote = voteService.findByPlaylistSongAndUser(playlistSong, user)
        if (existingVote != null) {
            if (existingVote.type == input.type) {
                return existingVote.toSchemaType()
            }
            val updatedVote = existingVote.copy(type = input.type)
            val updatedVoteResult = voteService.update(updatedVote)
            return updatedVoteResult?.toSchemaType()
        }

        val vote =
            Vote(
                playlistSong = playlistSong,
                user = user,
                type = input.type,
            )

        val createdVote = voteService.create(vote)
        return createdVote.toSchemaType()
    }

    suspend fun updateVote(
        id: UUID,
        input: VoteUpdateInput,
    ): VoteGQL? {
        val existingVote = voteService.getById(id) ?: return null
        val updatedVote =
            existingVote.copy(
                type =
                    when (val type = input.type) {
                        is OptionalInput.Defined -> type.value ?: existingVote.type
                        else -> existingVote.type
                    },
            )
        val updatedVoteResult = voteService.update(updatedVote)
        return updatedVoteResult?.toSchemaType()
    }

    suspend fun deleteVote(id: UUID): Boolean = voteService.delete(id)
}
