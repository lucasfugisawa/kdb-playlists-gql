package com.fugisawa.playlistsgql.infrastructure.data.mappers

import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.infrastructure.data.dao.UserDao

fun UserDao.toEntity(): User =
    User(
        id = this.id.value,
        username = this.username,
        passwordHash = this.passwordHash,
        roles = this.roles,
    )

fun User.toDao(): UserDao = UserDao.create(this.id, this.username, this.passwordHash, this.roles)

fun List<UserDao>.toEntities(): List<User> = this.map { it.toEntity() }
