package com.fugisawa.playlistsgql.infrastructure.data.mappers

import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.infrastructure.data.dao.PlaylistSongDao

fun PlaylistSongDao.toEntity(): PlaylistSong =
    PlaylistSong(
        id = this.id.value,
        playlist = this.playlist.toEntity(),
        song = this.song.toEntity(),
        addedBy = this.addedBy.toEntity(),
        position = this.position,
    )

fun PlaylistSong.toDao(): PlaylistSongDao {
    val playlistSong = this
    return PlaylistSongDao.new(playlistSong.id) {
        playlist = playlistSong.playlist.toDao()
        song = playlistSong.song.toDao()
        addedBy = playlistSong.addedBy.toDao()
        position = playlistSong.position
    }
}

fun List<PlaylistSongDao>.toEntities(): List<PlaylistSong> = this.map { it.toEntity() }
