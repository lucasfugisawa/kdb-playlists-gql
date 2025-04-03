package com.fugisawa.playlistsgql.infrastructure.graphql

import com.expediagroup.graphql.generator.extensions.plus
import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import com.fugisawa.playlistsgql.domain.models.User
import graphql.GraphQLContext
import io.ktor.server.request.ApplicationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.UUID

class CustomGraphQLContextFactory : DefaultKtorGraphQLContextFactory() {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override suspend fun generateContext(request: ApplicationRequest): GraphQLContext =
        super.generateContext(request).plus(
            mutableMapOf<Any, Any>(
                "user" to
                        User(
                            id = UUID.randomUUID(),
                            username = "test",
                        ),
                CoroutineScope::class to coroutineScope,
            ).also { map ->
                request.headers["my-custom-header"]?.let { customHeader ->
                    map["customHeader"] = customHeader
                }
            },
        )
}
