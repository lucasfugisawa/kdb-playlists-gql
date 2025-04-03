package com.fugisawa.playlistsgql.infrastructure.data.mappers

import com.fugisawa.playlistsgql.domain.entities.Vote
import com.fugisawa.playlistsgql.infrastructure.data.dao.VoteDao

fun VoteDao.toEntity(): Vote =
    Vote(
        id = this.id.value,
        playlistSong = this.playlistSong.toEntity(),
        user = this.user.toEntity(),
        type = this.type,
        createdAt = this.createdAt,
    )

fun Vote.toDao(): VoteDao {
    val vote = this
    return VoteDao.new(vote.id) {
        playlistSong = vote.playlistSong.toDao()
        user = vote.user.toDao()
        type = vote.type
        createdAt = vote.createdAt
    }
}

fun List<VoteDao>.toEntities(): List<Vote> = this.map { it.toEntity() }
