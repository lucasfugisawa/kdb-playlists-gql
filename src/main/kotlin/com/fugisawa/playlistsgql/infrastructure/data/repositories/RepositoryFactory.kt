package com.fugisawa.playlistsgql.infrastructure.data.repositories

import com.fugisawa.playlistsgql.domain.repositories.PlaylistRepository
import com.fugisawa.playlistsgql.domain.repositories.PlaylistSongRepository
import com.fugisawa.playlistsgql.domain.repositories.SongRepository
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import com.fugisawa.playlistsgql.domain.repositories.VoteRepository
import io.ktor.server.application.Application
import io.ktor.server.application.log

object RepositoryFactory {
    val songRepository: SongRepository by lazy { SongRepositoryImpl() }
    val userRepository: UserRepository by lazy { UserRepositoryImpl() }
    val playlistRepository: PlaylistRepository by lazy { PlaylistRepositoryImpl() }
    val playlistSongRepository: PlaylistSongRepository by lazy { PlaylistSongRepositoryImpl() }
    val voteRepository: VoteRepository by lazy { VoteRepositoryImpl() }
}

fun Application.configureRepositories() {
    RepositoryFactory.songRepository
    RepositoryFactory.userRepository
    RepositoryFactory.playlistRepository
    RepositoryFactory.playlistSongRepository
    RepositoryFactory.voteRepository

    log.info("Repositories configured")
}
