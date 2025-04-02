package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import java.util.UUID

data class PlaylistSongCreateInput(
    val playlistId: UUID,
    val songId: UUID,
    val addedById: UUID,
    val position: Int,
)
