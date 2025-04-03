package com.fugisawa.playlistsgql.domain.entities

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import java.time.Instant
import java.util.UUID

data class Vote(
    val id: UUID = UUID.randomUUID(),
    val playlistSong: PlaylistSong,
    val user: User,
    val type: VoteType,
    val createdAt: Instant = Instant.now(),
)
