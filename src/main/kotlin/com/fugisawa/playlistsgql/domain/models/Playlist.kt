package com.fugisawa.playlistsgql.domain.models

import java.time.Instant
import java.util.UUID

data class Playlist(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val creator: User,
    val createdAt: Instant = Instant.now(),
    val tags: List<String> = emptyList(),
)
