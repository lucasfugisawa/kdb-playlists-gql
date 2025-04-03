package com.fugisawa.playlistsgql.infrastructure.di

import com.fugisawa.playlistsgql.domain.repositories.PlaylistRepository
import com.fugisawa.playlistsgql.domain.repositories.PlaylistSongRepository
import com.fugisawa.playlistsgql.domain.repositories.SongRepository
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import com.fugisawa.playlistsgql.domain.repositories.VoteRepository
import com.fugisawa.playlistsgql.infrastructure.data.repositories.PlaylistRepositoryImpl
import com.fugisawa.playlistsgql.infrastructure.data.repositories.PlaylistSongRepositoryImpl
import com.fugisawa.playlistsgql.infrastructure.data.repositories.SongRepositoryImpl
import com.fugisawa.playlistsgql.infrastructure.data.repositories.UserRepositoryImpl
import com.fugisawa.playlistsgql.infrastructure.data.repositories.VoteRepositoryImpl
import com.fugisawa.playlistsgql.infrastructure.database.DatabaseConfig
import com.fugisawa.playlistsgql.infrastructure.graphql.CustomGraphQLContextFactory
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
import com.fugisawa.playlistsgql.infrastructure.graphql.subscriptions.ExampleSubscriptionService
import com.fugisawa.playlistsgql.services.PlaylistService
import com.fugisawa.playlistsgql.services.PlaylistSongService
import com.fugisawa.playlistsgql.services.SongService
import com.fugisawa.playlistsgql.services.UserService
import com.fugisawa.playlistsgql.services.VoteService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule =
    module {
        singleOf(::DatabaseConfig)
    }

val repositoryModule =
    module {
        singleOf(::SongRepositoryImpl) bind SongRepository::class
        singleOf(::UserRepositoryImpl) bind UserRepository::class
        singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class
        singleOf(::PlaylistSongRepositoryImpl) bind PlaylistSongRepository::class
        singleOf(::VoteRepositoryImpl) bind VoteRepository::class
    }

val serviceModule =
    module {
        singleOf(::SongService)
        singleOf(::UserService)
        singleOf(::PlaylistService)
        singleOf(::PlaylistSongService)
        singleOf(::VoteService)
    }

val dataLoaderModule =
    module {
        singleOf(::UserDataLoader)
        singleOf(::SongDataLoader)
        singleOf(::PlaylistDataLoader)
        singleOf(::PlaylistSongDataLoader)
        singleOf(::VoteDataLoader)
    }

val graphQLModule =
    module {
        singleOf(::ExampleSubscriptionService)

        singleOf(::PlaylistQueryService)
        singleOf(::SongQueryService)
        singleOf(::UserQueryService)
        singleOf(::PlaylistSongQueryService)
        singleOf(::VoteQueryService)

        singleOf(::PlaylistMutationService)
        singleOf(::SongMutationService)
        singleOf(::UserMutationService)
        singleOf(::PlaylistSongMutationService)
        singleOf(::VoteMutationService)

        singleOf(::CustomGraphQLContextFactory)
    }

val appModules =
    listOf(
        databaseModule,
        repositoryModule,
        serviceModule,
        dataLoaderModule,
        graphQLModule,
    )
