package com.fugisawa.playlistsgql.domain.services

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.entities.Vote
import com.fugisawa.playlistsgql.domain.repositories.VoteRepository
import java.util.UUID

class VoteService(
    private val voteRepository: VoteRepository,
) {
    suspend fun getById(id: UUID): Vote? = voteRepository.getById(id)

    suspend fun getAll(): List<Vote> = voteRepository.getAll()

    suspend fun findByPlaylistSong(playlistSong: PlaylistSong): List<Vote> = voteRepository.findByPlaylistSong(playlistSong)

    suspend fun findByUser(user: User): List<Vote> = voteRepository.findByUser(user)

    suspend fun findByPlaylistSongAndUser(
        playlistSong: PlaylistSong,
        user: User,
    ): Vote? = voteRepository.findByPlaylistSongAndUser(playlistSong, user)

    suspend fun findByType(type: VoteType): List<Vote> = voteRepository.findByType(type)

    suspend fun create(vote: Vote): Vote = voteRepository.create(vote)

    suspend fun update(vote: Vote): Vote = voteRepository.update(vote)

    suspend fun delete(id: UUID): Boolean = voteRepository.delete(id)
}
