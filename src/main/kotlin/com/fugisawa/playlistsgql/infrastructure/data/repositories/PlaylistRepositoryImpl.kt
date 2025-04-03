package com.fugisawa.playlistsgql.infrastructure.data.repositories

import com.fugisawa.playlistsgql.domain.entities.Playlist
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistRepository
import com.fugisawa.playlistsgql.infrastructure.data.dao.PlaylistDao
import com.fugisawa.playlistsgql.infrastructure.data.dao.PlaylistTable
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toDao
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntities
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntity
import com.fugisawa.playlistsgql.infrastructure.database.DatabaseConfig
import java.util.UUID

class PlaylistRepositoryImpl(
    private val databaseConfig: DatabaseConfig,
) : PlaylistRepository {
    override suspend fun getById(id: UUID): Playlist? =
        databaseConfig.dbQuery {
            PlaylistDao.findById(id)?.toEntity()
        }

    override suspend fun getAll(): List<Playlist> =
        databaseConfig.dbQuery {
            PlaylistDao.all().toList().toEntities()
        }

    override suspend fun findByTitle(title: String): List<Playlist> =
        databaseConfig.dbQuery {
            PlaylistDao.find { PlaylistTable.title eq title }.toList().toEntities()
        }

    override suspend fun findByCreator(creator: User): List<Playlist> =
        databaseConfig.dbQuery {
            val creatorDao = creator.toDao()
            PlaylistDao.find { PlaylistTable.creator eq creatorDao.id }.toList().toEntities()
        }

    override suspend fun findByTag(tag: String): List<Playlist> =
        databaseConfig.dbQuery {
            PlaylistDao
                .all()
                .filter { playlist ->
                    playlist.tags.contains(tag)
                }.toList()
                .toEntities()
        }

    override suspend fun create(playlist: Playlist): Playlist =
        databaseConfig.dbQuery {
            val dao = playlist.toDao()
            dao.toEntity()
        }

    override suspend fun update(playlist: Playlist): Playlist =
        databaseConfig.dbQuery {
            val dao = PlaylistDao.findById(playlist.id) ?: throw IllegalArgumentException("Playlist not found")
            dao.title = playlist.title
            dao.description = playlist.description
            dao.creator = playlist.creator.toDao()
            dao.createdAt = playlist.createdAt
            dao.tags = playlist.tags
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        databaseConfig.dbQuery {
            val dao = PlaylistDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
