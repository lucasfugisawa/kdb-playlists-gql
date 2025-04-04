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
    var passwordHash: String? = null
    private var rolesString: String = "USER"
        get() = field
        set(value) {
            field = value
        }

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
