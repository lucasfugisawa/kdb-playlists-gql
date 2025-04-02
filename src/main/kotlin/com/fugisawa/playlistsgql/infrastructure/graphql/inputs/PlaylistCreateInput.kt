package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import java.util.UUID

data class PlaylistCreateInput(
    val title: String,
    val description: String? = null,
    val creatorId: UUID,
    val tags: List<String>? = null,
)
