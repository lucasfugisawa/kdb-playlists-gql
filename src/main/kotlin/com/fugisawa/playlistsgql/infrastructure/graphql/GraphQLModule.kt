package com.fugisawa.playlistsgql.infrastructure.graphql

import com.expediagroup.graphql.dataloader.KotlinDataLoaderRegistryFactory
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.defaultGraphQLStatusPages
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphQLSubscriptionsRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.fugisawa.playlistsgql.infrastructure.graphql.mutations.LoginMutationService
import com.fugisawa.playlistsgql.infrastructure.graphql.queries.HelloQueryService
import com.fugisawa.playlistsgql.infrastructure.graphql.subscriptions.ExampleSubscriptionService
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.jackson.JacksonWebsocketContentConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import kotlin.time.Duration.Companion.seconds

fun Application.configureGraphQL() {
    install(WebSockets) {
        pingPeriod = 1.seconds
        contentConverter = JacksonWebsocketContentConverter()
    }
    install(StatusPages) {
        defaultGraphQLStatusPages()
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        anyHost() // @TODO: Remove before production!
    }
    install(GraphQL) {
        schema {
            packages = listOf("com.fugisawa.playlistsgql")
            queries = listOf(HelloQueryService())
            mutations = listOf(LoginMutationService())
            subscriptions = listOf(ExampleSubscriptionService())
            hooks = CustomSchemaHooks()
        }

        engine {
            dataLoaderRegistryFactory = KotlinDataLoaderRegistryFactory()
        }

        server {
            contextFactory = CustomGraphQLContextFactory()
        }
    }

    routing {
        graphQLGetRoute()
        graphQLPostRoute()
        graphQLSubscriptionsRoute()
        graphiQLRoute()
        graphQLSDLRoute()
    }
}
