package com.fugisawa.playlistsgql.infrastructure.graphql

import com.expediagroup.graphql.dataloader.KotlinDataLoaderRegistryFactory
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.defaultGraphQLStatusPages
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphQLSubscriptionsRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders.PlaylistDataLoader
import com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders.PlaylistSongDataLoader
import com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders.SongDataLoader
import com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders.UserDataLoader
import com.fugisawa.playlistsgql.infrastructure.graphql.dataloaders.VoteDataLoader
import com.fugisawa.playlistsgql.infrastructure.graphql.mutations.PlaylistMutationService
import com.fugisawa.playlistsgql.infrastructure.graphql.mutations.PlaylistSongMutationService
import com.fugisawa.playlistsgql.infrastructure.graphql.mutations.SongMutationService
import com.fugisawa.playlistsgql.infrastructure.graphql.mutations.UserMutationService
import com.fugisawa.playlistsgql.infrastructure.graphql.mutations.VoteMutationService
import com.fugisawa.playlistsgql.infrastructure.graphql.queries.PlaylistQueryService
import com.fugisawa.playlistsgql.infrastructure.graphql.queries.PlaylistSongQueryService
import com.fugisawa.playlistsgql.infrastructure.graphql.queries.SongQueryService
import com.fugisawa.playlistsgql.infrastructure.graphql.queries.UserQueryService
import com.fugisawa.playlistsgql.infrastructure.graphql.queries.VoteQueryService
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
import org.koin.ktor.ext.inject
import kotlin.time.Duration.Companion.seconds

fun Application.configureGraphQL() {
    val playlistQueryService by inject<PlaylistQueryService>()
    val songQueryService by inject<SongQueryService>()
    val userQueryService by inject<UserQueryService>()
    val playlistSongQueryService by inject<PlaylistSongQueryService>()
    val voteQueryService by inject<VoteQueryService>()

    val playlistMutationService by inject<PlaylistMutationService>()
    val songMutationService by inject<SongMutationService>()
    val userMutationService by inject<UserMutationService>()
    val playlistSongMutationService by inject<PlaylistSongMutationService>()
    val voteMutationService by inject<VoteMutationService>()

    // val exampleSubscriptionService by inject<ExampleSubscriptionService>()

    val userDataLoader by inject<UserDataLoader>()
    val songDataLoader by inject<SongDataLoader>()
    val playlistDataLoader by inject<PlaylistDataLoader>()
    val playlistSongDataLoader by inject<PlaylistSongDataLoader>()
    val voteDataLoader by inject<VoteDataLoader>()

    val customGraphQLContextFactory by inject<CustomGraphQLContextFactory>()

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

            queries =
                listOf(
                    playlistQueryService,
                    songQueryService,
                    userQueryService,
                    playlistSongQueryService,
                    voteQueryService,
                )

            mutations =
                listOf(
                    playlistMutationService,
                    songMutationService,
                    userMutationService,
                    playlistSongMutationService,
                    voteMutationService,
                )

            // subscriptions = listOf(exampleSubscriptionService)

            hooks = CustomSchemaHooks()
        }

        engine {
            dataLoaderRegistryFactory =
                KotlinDataLoaderRegistryFactory(
                    userDataLoader,
                    songDataLoader,
                    playlistDataLoader,
                    playlistSongDataLoader,
                    voteDataLoader,
                )
        }

        server {
            contextFactory = customGraphQLContextFactory
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
