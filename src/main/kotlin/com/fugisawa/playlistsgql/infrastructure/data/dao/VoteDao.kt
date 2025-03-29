package com.fugisawa.playlistsgql.data.dao

import com.fugisawa.playlistsgql.data.models.enums.VoteType
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.UUID

object VoteTable : UUIDTable("votes") {
    val playlistSong = reference("playlist_song_id", PlaylistSongTable)
    val user = reference("user_id", UserTable)
    val voteType: Column<String> = varchar("vote_type", 10)
    val createdAt = timestamp("created_at")

    init {
        uniqueIndex("playlist_song_user_idx", playlistSong, user)
    }
}

class VoteDao(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<VoteDao>(VoteTable)

    var playlistSong by PlaylistSongDao referencedOn VoteTable.playlistSong
    var user by UserDao referencedOn VoteTable.user
    var voteType by VoteTable.voteType
    var createdAt by VoteTable.createdAt

    var type: VoteType
        get() = VoteType.valueOf(voteType)
        set(value) {
            voteType = value.name
        }
}
