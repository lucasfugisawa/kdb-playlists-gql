package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import java.time.Instant
import java.util.UUID

data class PlaylistFilter(
    val ids: List<UUID>? = null,
    val creatorId: UUID? = null,
    val tag: String? = null,
    val title: String? = null,
    val createdBefore: Instant? = null,
    val createdAfter: Instant? = null,
)
