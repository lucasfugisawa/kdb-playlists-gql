package com.fugisawa.playlistsgql.infrastructure.data.mappers

import com.fugisawa.playlistsgql.domain.entities.Playlist
import com.fugisawa.playlistsgql.infrastructure.data.dao.PlaylistDao

fun PlaylistDao.toEntity(): Playlist =
    Playlist(
        id = this.id.value,
        title = this.title,
        description = this.description,
        creator = this.creator.toEntity(),
        createdAt = this.createdAt,
        tags = this.tags,
    )

fun Playlist.toDao(): PlaylistDao {
    val playlist = this
    return PlaylistDao.new(playlist.id) {
        title = playlist.title
        description = playlist.description
        creator = playlist.creator.toDao()
        createdAt = playlist.createdAt
        tags = playlist.tags
    }
}

fun List<PlaylistDao>.toEntities(): List<Playlist> = this.map { it.toEntity() }
