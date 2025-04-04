package com.fugisawa.playlistsgql.infrastructure.graphql

import com.expediagroup.graphql.generator.extensions.plus
import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.entities.UserRole
import com.fugisawa.playlistsgql.infrastructure.security.JwtConfig
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
                CoroutineScope::class to coroutineScope,
            ).also { map ->
                // Extract JWT token from Authorization header
                val authHeader = request.headers["Authorization"]
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    val token = authHeader.substring(7) // Remove "Bearer " prefix
                    try {
                        // Validate token and extract user information
                        val userId = JwtConfig.getUserId(token)
                        val roles = JwtConfig.getRoles(token)

                        // Add JWT principal to context
                        map["jwtPrincipal"] =
                            object {
                                val userId = userId
                                val roles = roles
                            }

                        // Add user to context for backward compatibility
                        map["user"] =
                            User(
                                id = userId,
                                username = "authenticated-user", // You might want to fetch the actual username
                                roles = roles.toSet(),
                            )
                    } catch (_: Exception) {
                        // Token validation failed, use default user
                        map["user"] =
                            User(
                                id = UUID.randomUUID(),
                                username = "anonymous",
                                roles = setOf(UserRole.USER),
                            )
                    }
                } else {
                    // No token provided, use default user
                    map["user"] =
                        User(
                            id = UUID.randomUUID(),
                            username = "anonymous",
                            roles = setOf(UserRole.USER),
                        )
                }

                // Keep custom header handling
                request.headers["my-custom-header"]?.let { customHeader ->
                    map["customHeader"] = customHeader
                }
            },
        )
}
