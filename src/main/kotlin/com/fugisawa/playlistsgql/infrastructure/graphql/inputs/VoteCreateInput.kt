package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.fugisawa.playlistsgql.domain.entities.VoteType
import java.util.UUID

data class VoteCreateInput(
    val playlistSongId: UUID,
    val userId: UUID,
    val type: VoteType,
)
