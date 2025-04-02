package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.fugisawa.playlistsgql.data.models.enums.Genre
import java.util.UUID

data class SongFilter(
    val ids: List<UUID>? = null,
    val title: String? = null,
    val artist: String? = null,
    val genre: Genre? = null,
)
