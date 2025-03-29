package com.fugisawa.playlistsgql.data.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object UserTable : UUIDTable("users") {
    val username: Column<String> = varchar("username", 255).uniqueIndex()
}

class UserDao(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDao>(UserTable)

    var username by UserTable.username
}
