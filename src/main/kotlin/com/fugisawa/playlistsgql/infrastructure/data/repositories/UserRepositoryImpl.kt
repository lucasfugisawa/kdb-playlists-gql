package com.fugisawa.playlistsgql.infrastructure.data.repositories

import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.repositories.UserRepository
import com.fugisawa.playlistsgql.infrastructure.data.dao.UserDao
import com.fugisawa.playlistsgql.infrastructure.data.dao.UserTable
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toDao
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntities
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntity
import com.fugisawa.playlistsgql.infrastructure.database.DatabaseConfig
import java.util.UUID

class UserRepositoryImpl(
    private val databaseConfig: DatabaseConfig,
) : UserRepository {
    override suspend fun getById(id: UUID): User? =
        databaseConfig.dbQuery {
            UserDao.findById(id)?.toEntity()
        }

    override suspend fun getByIds(ids: List<UUID>): List<User> =
        databaseConfig.dbQuery {
            if (ids.isEmpty()) {
                emptyList()
            } else {
                ids.mapNotNull { id -> UserDao.findById(id)?.toEntity() }
            }
        }

    override suspend fun getAll(): List<User> =
        databaseConfig.dbQuery {
            UserDao.all().toList().toEntities()
        }

    override suspend fun findByUsername(username: String): User? =
        databaseConfig.dbQuery {
            UserDao.find { UserTable.username eq username }.firstOrNull()?.toEntity()
        }

    override suspend fun create(user: User): User =
        databaseConfig.dbQuery {
            val dao = user.toDao()
            dao.toEntity()
        }

    override suspend fun update(user: User): User =
        databaseConfig.dbQuery {
            val dao = UserDao.findById(user.id) ?: throw IllegalArgumentException("User not found")
            dao.username = user.username
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        databaseConfig.dbQuery {
            val dao = UserDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
