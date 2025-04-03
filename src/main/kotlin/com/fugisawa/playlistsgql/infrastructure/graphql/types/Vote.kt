package com.fugisawa.playlistsgql.infrastructure.graphql.types

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import com.fugisawa.playlistsgql.domain.models.Vote
import java.time.Instant
import java.util.UUID

data class VoteGQL(
    val id: UUID,
    val playlistSong: com.fugisawa.playlistsgql.infrastructure.graphql.types.PlaylistSong,
    val user: User,
    val type: VoteType,
    val createdAt: Instant,
)

fun Vote.toSchemaType(): VoteGQL =
    VoteGQL(
        id = id,
        playlistSong = playlistSong.toSchemaType(),
        user = user.toSchemaType(),
        type = type,
        createdAt = createdAt,
    )

fun VoteGQL.toEntity(): Vote =
    Vote(
        id = id,
        playlistSong = playlistSong.toEntity(),
        user = user.toEntity(),
        type = type,
        createdAt = createdAt,
    )
