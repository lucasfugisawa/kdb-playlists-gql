package com.fugisawa.playlistsgql.infrastructure.data.dao

import com.fugisawa.playlistsgql.domain.entities.Genre
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object SongTable : UUIDTable("songs") {
    val title: Column<String> = varchar("title", 255)
    val artist: Column<String> = varchar("artist", 255)
    val duration: Column<Int> = integer("duration")
    val genre: Column<String> = varchar("genre", 50)
}

class SongDao(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SongDao>(SongTable)

    var title by SongTable.title
    var artist by SongTable.artist
    var duration by SongTable.duration
    var genre by SongTable.genre

    var genreEnum: Genre
        get() = Genre.valueOf(genre)
        set(value) {
            genre = value.name
        }
}
