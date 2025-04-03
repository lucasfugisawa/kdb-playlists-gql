package com.fugisawa.playlistsgql.infrastructure.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.domain.services.VoteService
import com.fugisawa.playlistsgql.infrastructure.graphql.types.VoteGQL
import com.fugisawa.playlistsgql.infrastructure.graphql.types.toSchemaType
import java.util.UUID
import com.fugisawa.playlistsgql.data.models.enums.VoteType
import java.time.Instant
import com.fugisawa.playlistsgql.infrastructure.graphql.types.GraphQLCollection
import com.fugisawa.playlistsgql.infrastructure.graphql.types.PageInfo
import com.fugisawa.playlistsgql.infrastructure.graphql.types.pagedCollection

data class VoteCollection(
    override val items: List<VoteGQL>,
    override val totalCount: Int,
    override val pageInfo: PageInfo
) : GraphQLCollection<VoteGQL>

class VoteQueryService(
    private val voteService: VoteService,
    private val playlistSongService: PlaylistSongService,
    private val userService: UserService,
) : Query {
    suspend fun vote(id: UUID): VoteGQL? = voteService.getById(id)?.toSchemaType()

    suspend fun votes(
        filter: VoteFilter? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): VoteCollection {
        val allVotes = voteService.getAll()

        val filteredVotes =
            allVotes.filter { vote ->
                (filter?.ids == null || filter.ids.contains(vote.id)) &&
                    (filter?.userId == null || vote.user.id == filter.userId) &&
                    (filter?.playlistSongId == null || vote.playlistSong.id == filter.playlistSongId) &&
                    (filter?.type == null || vote.type == filter.type) &&
                    (filter?.createdBefore == null || vote.createdAt.isBefore(filter.createdBefore)) &&
                    (filter?.createdAfter == null || vote.createdAt.isAfter(filter.createdAfter))
            }

        val (items, totalCount, pageInfo) = pagedCollection(
            allItems = allVotes,
            filteredItems = filteredVotes,
            offset = offset,
            limit = limit,
            transform = { it.toSchemaType() }
        )

        return VoteCollection(
            items = items,
            totalCount = totalCount,
            pageInfo = pageInfo
        )
    }
}

data class VoteFilter(
    val ids: List<UUID>? = null,
    val userId: UUID? = null,
    val playlistSongId: UUID? = null,
    val type: VoteType? = null,
    val createdBefore: Instant? = null,
    val createdAfter: Instant? = null,
)
