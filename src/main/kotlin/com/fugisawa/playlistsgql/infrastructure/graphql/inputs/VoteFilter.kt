package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import java.time.Instant
import java.util.UUID

data class VoteFilter(
    val ids: List<UUID>? = null,
    val userId: UUID? = null,
    val playlistSongId: UUID? = null,
    val type: VoteType? = null,
    val createdBefore: Instant? = null,
    val createdAfter: Instant? = null,
)
