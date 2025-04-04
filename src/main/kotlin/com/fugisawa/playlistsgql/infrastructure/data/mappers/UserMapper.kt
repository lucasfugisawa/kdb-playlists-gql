package com.fugisawa.playlistsgql.infrastructure.data.mappers

import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.infrastructure.data.dao.UserDao

fun UserDao.toEntity(): User =
    User(
        id = this.id.value,
        username = this.username,
        roles = this.roles,
    )

fun User.toDao(): UserDao = UserDao.create(this.id, this.username, this.roles)

fun List<UserDao>.toEntities(): List<User> = this.map { it.toEntity() }
