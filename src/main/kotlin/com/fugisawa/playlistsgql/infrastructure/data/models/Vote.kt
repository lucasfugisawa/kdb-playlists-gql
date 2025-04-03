package com.fugisawa.playlistsgql.infrastructure.data.models

import com.fugisawa.playlistsgql.domain.entities.VoteType
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.UUID

object Votes : UUIDTable("votes") {
    val playlistSong = reference("playlist_song_id", PlaylistSongs)
    val user = reference("user_id", Users)
    val voteType: Column<String> = varchar("vote_type", 10)
    val createdAt = timestamp("created_at")

    init {
        uniqueIndex("playlist_song_user_idx", playlistSong, user)
    }
}

class Vote(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Vote>(Votes)

    var playlistSong by PlaylistSong referencedOn Votes.playlistSong
    var user by User referencedOn Votes.user
    var voteType by Votes.voteType
    var createdAt by Votes.createdAt

    var type: VoteType
        get() = VoteType.valueOf(voteType)
        set(value) {
            voteType = value.name
        }
}
