package com.fugisawa.playlistsgql.infrastructure.data.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object PlaylistSongTable : UUIDTable("playlist_songs") {
    val playlist = reference("playlist_id", PlaylistTable)
    val song = reference("song_id", SongTable)
    val addedBy = reference("added_by_id", UserTable)
    val position: Column<Int> = integer("position")

    init {
        uniqueIndex("playlist_position_idx", playlist, position)
    }
}

class PlaylistSongDao(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlaylistSongDao>(PlaylistSongTable)

    var playlist by PlaylistDao referencedOn PlaylistSongTable.playlist
    var song by SongDao.Companion referencedOn PlaylistSongTable.song
    var addedBy by UserDao.Companion referencedOn PlaylistSongTable.addedBy
    var position by PlaylistSongTable.position
}
