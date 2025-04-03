package com.fugisawa.playlistsgql.domain.entities

import com.fugisawa.playlistsgql.data.models.enums.Genre
import java.util.UUID

data class Song(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val artist: String,
    val duration: Int,
    val genre: Genre,
)
