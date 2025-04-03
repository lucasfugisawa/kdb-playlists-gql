package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import com.fugisawa.playlistsgql.domain.entities.Genre

data class SongCreateInput(
    val title: String,
    val artist: String,
    val duration: Int,
    val genre: Genre,
)
