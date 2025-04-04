package com.fugisawa.playlistsgql.infrastructure.graphql.types

class LoginResponse(
    val token: String,
    val user: User,
)
