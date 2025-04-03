package com.fugisawa.playlistsgql.infrastructure.data.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object PlaylistSongs : UUIDTable("playlist_songs") {
    val playlist = reference("playlist_id", Playlists)
    val song = reference("song_id", Songs)
    val addedBy = reference("added_by_id", Users)
    val position: Column<Int> = integer("position")

    init {
        uniqueIndex("playlist_position_idx", playlist, position)
    }
}

class PlaylistSong(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlaylistSong>(PlaylistSongs)

    var playlist by Playlist referencedOn PlaylistSongs.playlist
    var song by Song.Companion referencedOn PlaylistSongs.song
    var addedBy by User.Companion referencedOn PlaylistSongs.addedBy
    var position by PlaylistSongs.position
}
