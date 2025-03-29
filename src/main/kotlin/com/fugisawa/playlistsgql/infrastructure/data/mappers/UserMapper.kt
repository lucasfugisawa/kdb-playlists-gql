package com.fugisawa.playlistsgql.data.mappers

import com.fugisawa.playlistsgql.data.dao.UserDao
import com.fugisawa.playlistsgql.domain.models.User

fun UserDao.toEntity(): User =
    User(
        id = this.id.value,
        username = this.username,
    )

fun User.toDao(): UserDao =
    UserDao.new(this.id) {
        username = this.username
    }

fun List<UserDao>.toEntities(): List<User> = this.map { it.toEntity() }
