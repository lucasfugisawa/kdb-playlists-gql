package com.fugisawa.playlistsgql.data.repositories

import com.fugisawa.playlistsgql.data.dao.UserDao
import com.fugisawa.playlistsgql.data.dao.UserTable
import com.fugisawa.playlistsgql.data.mappers.toDao
import com.fugisawa.playlistsgql.data.mappers.toEntities
import com.fugisawa.playlistsgql.data.mappers.toEntity
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import com.fugisawa.playlistsgql.infrastructure.config.database.DatabaseFactory.dbQuery
import java.util.UUID

class UserRepositoryImpl : UserRepository {
    override suspend fun getById(id: UUID): User? =
        dbQuery {
            UserDao.findById(id)?.toEntity()
        }

    override suspend fun getAll(): List<User> =
        dbQuery {
            UserDao.all().toList().toEntities()
        }

    override suspend fun findByUsername(username: String): User? =
        dbQuery {
            UserDao.find { UserTable.username eq username }.firstOrNull()?.toEntity()
        }

    override suspend fun create(user: User): User =
        dbQuery {
            val dao = user.toDao()
            dao.toEntity()
        }

    override suspend fun update(user: User): User =
        dbQuery {
            val dao = UserDao.findById(user.id) ?: throw IllegalArgumentException("User not found")
            dao.username = user.username
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        dbQuery {
            val dao = UserDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
