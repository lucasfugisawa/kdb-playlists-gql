package com.fugisawa.playlistsgql.infrastructure.data.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.UUID

object Playlists : UUIDTable("playlists") {
    val title: Column<String> = varchar("title", 255)
    val description: Column<String?> = text("description").nullable()
    val creator = reference("creator_id", Users)
    val createdAt = timestamp("created_at")
    val tags = text("tags").nullable()
}

class Playlist(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Playlist>(Playlists)

    var title by Playlists.title
    var description by Playlists.description
    var creator by User.Companion referencedOn Playlists.creator
    var createdAt by Playlists.createdAt
    private var tagsString by Playlists.tags

    var tags: List<String>
        get() = tagsString?.split(",")?.map { it.trim() } ?: emptyList()
        set(value) {
            tagsString = if (value.isEmpty()) null else value.joinToString(",")
        }
}
