package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.fugisawa.playlistsgql.data.models.enums.Genre

data class SongCreateInput(
    val title: String,
    val artist: String,
    val duration: Int,
    val genre: Genre,
)
