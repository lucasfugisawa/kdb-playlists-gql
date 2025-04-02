package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.models.Vote
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.domain.services.VoteService
import com.fugisawa.playlistsgql.infrastructure.graphql.inputs.VoteFilter
import java.util.UUID

class VoteQueryService(
    private val voteService: VoteService,
    private val playlistSongService: PlaylistSongService,
    private val userService: UserService,
) : Query {
    suspend fun vote(id: UUID): Vote? {
        return voteService.getById(id)
    }

    suspend fun votes(filter: VoteFilter? = null, limit: Int? = null, offset: Int? = null): List<Vote> {
        val allVotes = voteService.getAll()

        val filteredVotes = allVotes.filter { vote ->
            (filter?.ids == null || filter.ids.contains(vote.id)) &&
            (filter?.userId == null || vote.user.id == filter.userId) &&
            (filter?.playlistSongId == null || vote.playlistSong.id == filter.playlistSongId) &&
            (filter?.type == null || vote.type == filter.type) &&
            (filter?.createdBefore == null || vote.createdAt.isBefore(filter.createdBefore)) &&
            (filter?.createdAfter == null || vote.createdAt.isAfter(filter.createdAfter))
        }

        return filteredVotes
            .let { if (offset != null) it.drop(offset) else it }
            .let { if (limit != null) it.take(limit) else it }
    }
}
