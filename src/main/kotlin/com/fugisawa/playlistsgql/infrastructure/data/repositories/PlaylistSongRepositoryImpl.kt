package com.fugisawa.playlistsgql.infrastructure.data.repositories

import com.fugisawa.playlistsgql.domain.entities.Playlist
import com.fugisawa.playlistsgql.domain.entities.PlaylistSong
import com.fugisawa.playlistsgql.domain.entities.Song
import com.fugisawa.playlistsgql.domain.entities.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistSongRepository
import com.fugisawa.playlistsgql.infrastructure.data.dao.PlaylistSongDao
import com.fugisawa.playlistsgql.infrastructure.data.dao.PlaylistSongTable
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toDao
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntities
import com.fugisawa.playlistsgql.infrastructure.data.mappers.toEntity
import com.fugisawa.playlistsgql.infrastructure.database.DatabaseConfig
import org.jetbrains.exposed.sql.and
import java.util.UUID

class PlaylistSongRepositoryImpl(
    private val databaseConfig: DatabaseConfig,
) : PlaylistSongRepository {
    override suspend fun getById(id: UUID): PlaylistSong? =
        databaseConfig.dbQuery {
            PlaylistSongDao.findById(id)?.toEntity()
        }

    override suspend fun getByIds(ids: List<UUID>): List<PlaylistSong> =
        databaseConfig.dbQuery {
            if (ids.isEmpty()) {
                emptyList()
            } else {
                ids.mapNotNull { id -> PlaylistSongDao.findById(id)?.toEntity() }
            }
        }

    override suspend fun getAll(): List<PlaylistSong> =
        databaseConfig.dbQuery {
            PlaylistSongDao.all().toList().toEntities()
        }

    override suspend fun findByPlaylist(playlist: Playlist): List<PlaylistSong> =
        databaseConfig.dbQuery {
            val playlistDao = playlist.toDao()
            PlaylistSongDao.find { PlaylistSongTable.playlist eq playlistDao.id }.toList().toEntities()
        }

    override suspend fun findBySong(song: Song): List<PlaylistSong> =
        databaseConfig.dbQuery {
            val songDao = song.toDao()
            PlaylistSongDao.find { PlaylistSongTable.song eq songDao.id }.toList().toEntities()
        }

    override suspend fun findByAddedBy(user: User): List<PlaylistSong> =
        databaseConfig.dbQuery {
            val userDao = user.toDao()
            PlaylistSongDao.find { PlaylistSongTable.addedBy eq userDao.id }.toList().toEntities()
        }

    override suspend fun findByPlaylistAndPosition(
        playlist: Playlist,
        position: Int,
    ): PlaylistSong? =
        databaseConfig.dbQuery {
            val playlistDao = playlist.toDao()
            PlaylistSongDao
                .find {
                    (PlaylistSongTable.playlist eq playlistDao.id) and (PlaylistSongTable.position eq position)
                }.firstOrNull()
                ?.toEntity()
        }

    override suspend fun create(playlistSong: PlaylistSong): PlaylistSong =
        databaseConfig.dbQuery {
            val dao = playlistSong.toDao()
            dao.toEntity()
        }

    override suspend fun update(playlistSong: PlaylistSong): PlaylistSong =
        databaseConfig.dbQuery {
            val dao = PlaylistSongDao.findById(playlistSong.id) ?: throw IllegalArgumentException("PlaylistSong not found")
            dao.playlist = playlistSong.playlist.toDao()
            dao.song = playlistSong.song.toDao()
            dao.addedBy = playlistSong.addedBy.toDao()
            dao.position = playlistSong.position
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        databaseConfig.dbQuery {
            val dao = PlaylistSongDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
