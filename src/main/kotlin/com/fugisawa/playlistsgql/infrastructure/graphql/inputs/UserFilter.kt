package com.fugisawa.playlistsgql.infrastructure.graphql.inputs

import java.util.UUID

data class UserFilter(
    val ids: List<UUID>? = null,
    val username: String? = null,
    val email: String? = null,
)
