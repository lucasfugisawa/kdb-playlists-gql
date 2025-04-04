package com.fugisawa.playlistsgql.infrastructure.data.dao

import com.fugisawa.playlistsgql.domain.entities.UserRole
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object UserTable : UUIDTable("users") {
    val username: Column<String> = varchar("username", 255).uniqueIndex()
    val passwordHash: Column<String?> = varchar("password_hash", 255).nullable()
    val roles: Column<String> = varchar("roles", 255).default("USER")
}

class UserDao(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDao>(UserTable) {
        fun create(
            id: UUID,
            username: String,
            passwordHash: String? = null,
            roles: Set<UserRole> = setOf(UserRole.USER),
        ): UserDao =
            new(id) {
                this.username = username
                this.passwordHash = passwordHash
                this.roles = roles
            }
    }

    var username by UserTable.username
    var passwordHash by UserTable.passwordHash
    private var rolesString by UserTable.roles

    var roles: Set<UserRole>
        get() =
            rolesString
                .split(",")
                .mapNotNull { roleName ->
                    try {
                        UserRole.valueOf(roleName.trim())
                    } catch (_: IllegalArgumentException) {
                        null
                    }
                }.toSet()
        set(value) {
            rolesString = value.joinToString(",") { it.name }
        }
}
