package com.fugisawa.playlistsgql.domain.repositories

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.models.Vote
import java.util.UUID

interface VoteRepository {
    suspend fun getById(id: UUID): Vote?

    suspend fun getAll(): List<Vote>

    suspend fun findByPlaylistSong(playlistSong: PlaylistSong): List<Vote>

    suspend fun findByUser(user: User): List<Vote>

    suspend fun findByPlaylistSongAndUser(
        playlistSong: PlaylistSong,
        user: User,
    ): Vote?

    suspend fun findByType(type: VoteType): List<Vote>

    suspend fun create(vote: Vote): Vote

    suspend fun update(vote: Vote): Vote

    suspend fun delete(id: UUID): Boolean
}
