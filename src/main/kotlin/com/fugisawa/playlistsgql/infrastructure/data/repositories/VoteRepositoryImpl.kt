package com.fugisawa.playlistsgql.infrastructure.data.repositories

import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.entities.Vote
import com.fugisawa.playlistsgql.domain.entities.VoteType
import com.fugisawa.playlistsgql.domain.repositories.VoteRepository
import com.fugisawa.playlistsgql.infrastructure.data.dao.VoteDao
import com.fugisawa.playlistsgql.infrastructure.data.dao.VoteTable
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toDao
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntities
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntity
import com.fugisawa.playlistsgql.infrastructure.database.DatabaseConfig
import org.jetbrains.exposed.sql.and
import java.util.UUID

class VoteRepositoryImpl(
    private val databaseConfig: DatabaseConfig,
) : VoteRepository {
    override suspend fun getById(id: UUID): Vote? =
        databaseConfig.dbQuery {
            VoteDao.findById(id)?.toEntity()
        }

    override suspend fun getByIds(ids: List<UUID>): List<Vote> =
        databaseConfig.dbQuery {
            if (ids.isEmpty()) {
                emptyList()
            } else {
                ids.mapNotNull { id -> VoteDao.findById(id)?.toEntity() }
            }
        }

    override suspend fun getAll(): List<Vote> =
        databaseConfig.dbQuery {
            VoteDao.all().toList().toEntities()
        }

    override suspend fun findByPlaylistSong(playlistSong: PlaylistSong): List<Vote> =
        databaseConfig.dbQuery {
            val playlistSongDao = playlistSong.toDao()
            VoteDao.find { VoteTable.playlistSong eq playlistSongDao.id }.toList().toEntities()
        }

    override suspend fun findByUser(user: User): List<Vote> =
        databaseConfig.dbQuery {
            val userDao = user.toDao()
            VoteDao.find { VoteTable.user eq userDao.id }.toList().toEntities()
        }

    override suspend fun findByPlaylistSongAndUser(
        playlistSong: PlaylistSong,
        user: User,
    ): Vote? =
        databaseConfig.dbQuery {
            val playlistSongDao = playlistSong.toDao()
            val userDao = user.toDao()
            VoteDao
                .find {
                    (VoteTable.playlistSong eq playlistSongDao.id) and (VoteTable.user eq userDao.id)
                }.firstOrNull()
                ?.toEntity()
        }

    override suspend fun findByType(type: VoteType): List<Vote> =
        databaseConfig.dbQuery {
            VoteDao.find { VoteTable.voteType eq type.name }.toList().toEntities()
        }

    override suspend fun create(vote: Vote): Vote =
        databaseConfig.dbQuery {
            val dao = vote.toDao()
            dao.toEntity()
        }

    override suspend fun update(vote: Vote): Vote =
        databaseConfig.dbQuery {
            val dao = VoteDao.findById(vote.id) ?: throw IllegalArgumentException("Vote not found")
            dao.playlistSong = vote.playlistSong.toDao()
            dao.user = vote.user.toDao()
            dao.type = vote.type
            dao.createdAt = vote.createdAt
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        databaseConfig.dbQuery {
            val dao = VoteDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
