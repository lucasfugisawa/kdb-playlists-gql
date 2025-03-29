package com.fugisawa.playlistsgql.data.mappers

import com.fugisawa.playlistsgql.data.dao.SongDao
import com.fugisawa.playlistsgql.domain.models.Song

fun SongDao.toEntity(): Song =
    Song(
        id = this.id.value,
        title = this.title,
        artist = this.artist,
        duration = this.duration,
        genre = this.genreEnum,
    )

fun Song.toDao(): SongDao {
    val song = this
    return SongDao.new(song.id) {
        title = song.title
        artist = song.artist
        duration = song.duration
        genreEnum = song.genre
    }
}

fun List<SongDao>.toEntities(): List<Song> = this.map { it.toEntity() }
