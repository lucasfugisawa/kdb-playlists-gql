package com.fugisawa.playlistsgql.domain.models

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val username: String,
)
