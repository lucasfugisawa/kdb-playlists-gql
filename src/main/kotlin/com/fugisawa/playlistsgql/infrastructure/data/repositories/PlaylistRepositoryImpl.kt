package com.fugisawa.playlistsgql.data.repositories

import com.fugisawa.playlistsgql.data.dao.PlaylistDao
import com.fugisawa.playlistsgql.data.dao.PlaylistTable
import com.fugisawa.playlistsgql.data.mappers.toDao
import com.fugisawa.playlistsgql.data.mappers.toEntities
import com.fugisawa.playlistsgql.data.mappers.toEntity
import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistRepository
import com.fugisawa.playlistsgql.infrastructure.config.database.DatabaseFactory.dbQuery
import java.util.UUID

class PlaylistRepositoryImpl : PlaylistRepository {
    override suspend fun getById(id: UUID): Playlist? =
        dbQuery {
            PlaylistDao.findById(id)?.toEntity()
        }

    override suspend fun getAll(): List<Playlist> =
        dbQuery {
            PlaylistDao.all().toList().toEntities()
        }

    override suspend fun findByTitle(title: String): List<Playlist> =
        dbQuery {
            PlaylistDao.find { PlaylistTable.title eq title }.toList().toEntities()
        }

    override suspend fun findByCreator(creator: User): List<Playlist> =
        dbQuery {
            val creatorDao = creator.toDao()
            PlaylistDao.find { PlaylistTable.creator eq creatorDao.id }.toList().toEntities()
        }

    override suspend fun findByTag(tag: String): List<Playlist> =
        dbQuery {
            // This is a simple implementation that checks if the tag is contained in the tags string
            // A more sophisticated implementation would use a proper array type or a separate tags table
            PlaylistDao
                .all()
                .filter { playlist ->
                    playlist.tags.contains(tag)
                }.toList()
                .toEntities()
        }

    override suspend fun create(playlist: Playlist): Playlist =
        dbQuery {
            val dao = playlist.toDao()
            dao.toEntity()
        }

    override suspend fun update(playlist: Playlist): Playlist =
        dbQuery {
            val dao = PlaylistDao.findById(playlist.id) ?: throw IllegalArgumentException("Playlist not found")
            dao.title = playlist.title
            dao.description = playlist.description
            dao.creator = playlist.creator.toDao()
            dao.createdAt = playlist.createdAt
            dao.tags = playlist.tags
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        dbQuery {
            val dao = PlaylistDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
