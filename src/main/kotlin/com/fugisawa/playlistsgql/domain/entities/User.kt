package com.fugisawa.playlistsgql.domain.entities

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val roles: Set<UserRole> = setOf(UserRole.USER),
)
