package com.fugisawa.playlistsgql.graphql.queries

import com.expediagroup.graphql.server.operations.Query

class HelloQueryService : Query {
    fun hello() = "World!"
}
