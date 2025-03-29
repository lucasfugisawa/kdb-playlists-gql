package com.fugisawa.playlistsgql.data.repositories

import com.fugisawa.playlistsgql.data.dao.SongDao
import com.fugisawa.playlistsgql.data.dao.SongTable
import com.fugisawa.playlistsgql.data.mappers.toDao
import com.fugisawa.playlistsgql.data.mappers.toEntities
import com.fugisawa.playlistsgql.data.mappers.toEntity
import com.fugisawa.playlistsgql.data.models.enums.Genre
import com.fugisawa.playlistsgql.domain.models.Song
import com.fugisawa.playlistsgql.domain.repositories.SongRepository
import com.fugisawa.playlistsgql.infrastructure.config.database.DatabaseFactory.dbQuery
import java.util.UUID

class SongRepositoryImpl : SongRepository {
    override suspend fun getById(id: UUID): Song? =
        dbQuery {
            SongDao.findById(id)?.toEntity()
        }

    override suspend fun getAll(): List<Song> =
        dbQuery {
            SongDao.all().toList().toEntities()
        }

    override suspend fun findByTitle(title: String): List<Song> =
        dbQuery {
            SongDao.find { SongTable.title eq title }.toList().toEntities()
        }

    override suspend fun findByArtist(artist: String): List<Song> =
        dbQuery {
            SongDao.find { SongTable.artist eq artist }.toList().toEntities()
        }

    override suspend fun findByGenre(genre: Genre): List<Song> =
        dbQuery {
            SongDao.find { SongTable.genre eq genre.name }.toList().toEntities()
        }

    override suspend fun create(song: Song): Song =
        dbQuery {
            val dao = song.toDao()
            dao.toEntity()
        }

    override suspend fun update(song: Song): Song =
        dbQuery {
            val dao = SongDao.findById(song.id) ?: throw IllegalArgumentException("Song not found")
            dao.title = song.title
            dao.artist = song.artist
            dao.duration = song.duration
            dao.genreEnum = song.genre
            dao.toEntity()
        }

    override suspend fun delete(id: UUID): Boolean =
        dbQuery {
            val dao = SongDao.findById(id) ?: return@dbQuery false
            dao.delete()
            true
        }
}
