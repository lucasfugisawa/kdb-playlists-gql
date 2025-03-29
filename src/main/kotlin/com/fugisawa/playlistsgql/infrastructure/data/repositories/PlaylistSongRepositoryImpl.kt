package com.fugisawa.playlistsgql.data.repositories

import com.fugisawa.playlistsgql.data.dao.PlaylistSongDao
import com.fugisawa.playlistsgql.data.dao.PlaylistSongTable
import com.fugisawa.playlistsgql.data.mappers.toDao
import com.fugisawa.playlistsgql.data.mappers.toEntities
import com.fugisawa.playlistsgql.data.mappers.toEntity
import com.fugisawa.playlistsgql.domain.models.Playlist
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.models.User
import com.fugisawa.playlistsgql.domain.repositories.PlaylistSongRepository
import com.fugisawa.playlistsgql.infrastructure.config.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.and
import java.util.UUID

class PlaylistSongRepositoryImpl : PlaylistSongRepository {
    override suspend fun getById(id: UUID): PlaylistSong? =
        dbQuery {
            PlaylistSongDao.findById(id)?.toEntity()
        }

    override suspend fun getAll(): List<PlaylistSong> =
        dbQuery {
            PlaylistSongDao.all().toList().toEntities()
        }

    override suspend fun findByPlaylist(playlist: Playlist): List<PlaylistSong> =
        dbQuery {
            val playlistDao = playlist.toDao()
            PlaylistSongDao.find { PlaylistSongTable.playlist eq playlistDao.id }.toList().toEntities()
        }

    override suspend fun findBySong(song: Song): List<PlaylistSong> =
        dbQuery {
            val songDao = song.toDao()
            PlaylistSongDao.find { PlaylistSongTable.song eq songDao.id }.toList().toEntities()
        }

    override suspend fun findByAddedBy(user: User): List<PlaylistSong> =
        dbQuery {
            val userDao = user.toDao()
            PlaylistSongDao.find { PlaylistSongTable.addedBy eq userDao.id }.toList().toEntities()
        }

    override suspend fun findByPlaylistAndPosition(
        playlist: Playlist,
        position: Int,
    ): PlaylistSong? =
        dbQuery {
            val playlistDao = playlist.toDao()
            PlaylistSongDao
                .find {
                    (PlaylistSongTable.playlist eq playlistDao.id) and (PlaylistSongTable.position eq position)
                }.firstOrNull()
                ?.toEntity()
        }

    override suspend fun create(playlistSong: PlaylistSong): PlaylistSong =
        dbQuery {
            val dao = playlistSong.toDao()
            dao.toEntity()
        }

    override suspend fun update(playlistSong: PlaylistSong): PlaylistSong =
        dbQuery {
            val dao = PlaylistSongDao.findById(playlistSong.id) ?: throw IllegalArgumentException("PlaylistSong not found")
            dao.playlist = playlistSong.playlist.toDao()
            dao.song = playlistSong.song.toDao()
            dao.addedBy = playlistSong.addedBy.toDao()
            dao.position = playlistSong.position
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        dbQuery {
            val dao = PlaylistSongDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
