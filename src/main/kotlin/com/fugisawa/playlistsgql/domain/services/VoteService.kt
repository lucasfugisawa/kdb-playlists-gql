package com.fugisawa.playlistsgql.domain.services

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.models.Vote
import com.fugisawa.playlistsgql.domain.repositories.VoteRepository
import java.util.UUID

class VoteService(private val voteRepository: VoteRepository) {
    suspend fun getById(id: UUID): Vote? {
        return voteRepository.getById(id)
    }

    suspend fun getAll(): List<Vote> {
        return voteRepository.getAll()
    }

    suspend fun findByPlaylistSong(playlistSong: PlaylistSong): List<Vote> {
        return voteRepository.findByPlaylistSong(playlistSong)
    }

    suspend fun findByUser(user: User): List<Vote> {
        return voteRepository.findByUser(user)
    }

    suspend fun findByPlaylistSongAndUser(
        playlistSong: PlaylistSong,
        user: User,
    ): Vote? {
        return voteRepository.findByPlaylistSongAndUser(playlistSong, user)
    }

    suspend fun findByType(type: VoteType): List<Vote> {
        return voteRepository.findByType(type)
    }

    suspend fun create(vote: Vote): Vote {
        return voteRepository.create(vote)
    }

    suspend fun update(vote: Vote): Vote {
        return voteRepository.update(vote)
    }

    suspend fun delete(id: UUID): Boolean {
        return voteRepository.delete(id)
    }
}