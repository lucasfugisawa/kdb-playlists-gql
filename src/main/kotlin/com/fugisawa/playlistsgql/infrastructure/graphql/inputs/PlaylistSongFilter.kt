package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import java.util.UUID

data class PlaylistSongFilter(
    val ids: List<UUID>? = null,
    val playlistId: UUID? = null,
    val songId: UUID? = null,
    val addedById: UUID? = null,
    val position: Int? = null,
)
