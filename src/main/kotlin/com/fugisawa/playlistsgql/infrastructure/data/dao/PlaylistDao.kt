package com.fugisawa.playlistsgql.data.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.UUID

object PlaylistTable : UUIDTable("playlists") {
    val title: Column<String> = varchar("title", 255)
    val description: Column<String?> = text("description").nullable()
    val creator = reference("creator_id", UserTable)
    val createdAt = timestamp("created_at")
    val tags = text("tags").nullable()
}

class PlaylistDao(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlaylistDao>(PlaylistTable)

    var title by PlaylistTable.title
    var description by PlaylistTable.description
    var creator by UserDao referencedOn PlaylistTable.creator
    var createdAt by PlaylistTable.createdAt
    private var tagsString by PlaylistTable.tags

    var tags: List<String>
        get() = tagsString?.split(",")?.map { it.trim() } ?: emptyList()
        set(value) {
            tagsString = if (value.isEmpty()) null else value.joinToString(",")
        }
}
