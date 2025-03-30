package com.fugisawa.playlistsgql.di

import com.fugisawa.playlistsgql.config.DatabaseConfig
import com.fugisawa.playlistsgql.data.repositories.PlaylistRepositoryImpl
import com.fugisawa.playlistsgql.data.repositories.PlaylistSongRepositoryImpl
import com.fugisawa.playlistsgql.data.repositories.SongRepositoryImpl
import com.fugisawa.playlistsgql.data.repositories.UserRepositoryImpl
import com.fugisawa.playlistsgql.data.repositories.VoteRepositoryImpl
import com.fugisawa.playlistsgql.domain.repositories.PlaylistRepository
import com.fugisawa.playlistsgql.domain.repositories.PlaylistSongRepository
import com.fugisawa.playlistsgql.domain.repositories.SongRepository
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import com.fugisawa.playlistsgql.domain.repositories.VoteRepository
import com.fugisawa.playlistsgql.graphql.context.CustomGraphQLContextFactory
import com.fugisawa.playlistsgql.graphql.mutations.LoginMutationService
import com.fugisawa.playlistsgql.graphql.queries.HelloQueryService
import com.fugisawa.playlistsgql.graphql.subscriptions.ExampleSubscriptionService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule =
    module {
        single { DatabaseConfig() }
    }

val repositoryModule =
    module {
        singleOf(::SongRepositoryImpl) bind SongRepository::class
        singleOf(::UserRepositoryImpl) bind UserRepository::class
        singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class
        singleOf(::PlaylistSongRepositoryImpl) bind PlaylistSongRepository::class
        singleOf(::VoteRepositoryImpl) bind VoteRepository::class
    }

val graphQLModule =
    module {
        single { HelloQueryService() }
        single { LoginMutationService() }
        single { ExampleSubscriptionService() }
        single { CustomGraphQLContextFactory() }
    }

val appModules =
    listOf(
        databaseModule,
        repositoryModule,
        graphQLModule,
    )
