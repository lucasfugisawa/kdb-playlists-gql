package com.fugisawa.playlistsgql.data.mappers

import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.infrastructure.data.dao.UserDao

fun UserDao.toEntity(): User =
    User(
        id = this.id.value,
        username = this.username,
    )

fun User.toDao(): UserDao = UserDao.create(this.id, this.username)

fun List<UserDao>.toEntities(): List<User> = this.map { it.toEntity() }
