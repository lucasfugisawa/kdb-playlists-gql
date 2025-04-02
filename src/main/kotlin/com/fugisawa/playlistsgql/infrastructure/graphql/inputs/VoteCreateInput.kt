package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import java.util.UUID

data class VoteCreateInput(
    val playlistSongId: UUID,
    val userId: UUID,
    val type: VoteType,
)