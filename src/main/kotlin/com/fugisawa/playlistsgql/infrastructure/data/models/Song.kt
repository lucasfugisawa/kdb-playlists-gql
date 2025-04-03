package com.fugisawa.playlistsgql.infrastructure.data.models

import com.fugisawa.playlistsgql.domain.entities.Genre
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object Songs : UUIDTable("songs") {
    val title: Column<String> = varchar("title", 255)
    val artist: Column<String> = varchar("artist", 255)
    val duration: Column<Int> = integer("duration")
    val genre: Column<String> = varchar("genre", 50)
}

class Song(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Song>(Songs)

    var title by Songs.title
    var artist by Songs.artist
    var duration by Songs.duration
    var genre by Songs.genre

    var genreEnum: Genre
        get() = Genre.valueOf(genre)
        set(value) {
            genre = value.name
        }
}
