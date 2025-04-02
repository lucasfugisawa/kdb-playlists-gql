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
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import com.fugisawa.playlistsgql.domain.services.VoteService
import com.fugisawa.playlistsgql.graphql.context.CustomGraphQLContextFactory
import com.fugisawa.playlistsgql.graphql.subscriptions.ExampleSubscriptionService
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

val serviceModule =
    module {
        single { SongService(get()) }
        single { UserService(get()) }
        single { PlaylistService(get()) }
        single { PlaylistSongService(get()) }
        single { VoteService(get()) }
    }

val dataLoaderModule =
    module {
        single { UserDataLoader(get()) }
        single { SongDataLoader(get()) }
        single { PlaylistDataLoader(get()) }
        single { PlaylistSongDataLoader(get()) }
        single { VoteDataLoader(get()) }
    }

val graphQLModule =
    module {
        single { ExampleSubscriptionService() }

        single { PlaylistQueryService(get(), get()) }
        single { SongQueryService(get()) }
        single { UserQueryService(get()) }
        single { PlaylistSongQueryService(get(), get(), get(), get()) }
        single { VoteQueryService(get(), get(), get()) }

        single { PlaylistMutationService(get(), get()) }
        single { SongMutationService(get()) }
        single { UserMutationService(get()) }
        single { PlaylistSongMutationService(get(), get(), get(), get()) }
        single { VoteMutationService(get(), get(), get()) }

        single { CustomGraphQLContextFactory() }
    }

val appModules =
    listOf(
        databaseModule,
        repositoryModule,
        serviceModule,
        dataLoaderModule,
        graphQLModule,
    )
