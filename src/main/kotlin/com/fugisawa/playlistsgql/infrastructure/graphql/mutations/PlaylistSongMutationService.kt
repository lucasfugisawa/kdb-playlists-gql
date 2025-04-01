package com.fugisawa.playlistsgql.infrastructure.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.fugisawa.playlistsgql.domain.models.PlaylistSong
import com.fugisawa.playlistsgql.domain.services.PlaylistService
import com.fugisawa.playlistsgql.domain.services.PlaylistSongService
import com.fugisawa.playlistsgql.domain.services.SongService
import com.fugisawa.playlistsgql.domain.services.UserService
import java.util.UUID

class PlaylistSongMutationService(
    private val playlistSongService: PlaylistSongService,
    private val playlistService: PlaylistService,
    private val songService: SongService,
    private val userService: UserService,
) : Mutation {
    suspend fun addSongToPlaylist(
        playlistId: UUID,
        songId: UUID,
        addedById: UUID,
        position: Int,
    ): PlaylistSong? {
        val playlist = playlistService.getById(playlistId) ?: return null
        val song = songService.getById(songId) ?: return null
        val addedBy = userService.getById(addedById) ?: return null
        
        val playlistSong = PlaylistSong(
            playlist = playlist,
            song = song,
            addedBy = addedBy,
            position = position,
        )
        
        return playlistSongService.create(playlistSong)
    }
    
    suspend fun updatePlaylistSongPosition(
        id: UUID,
        position: Int,
    ): PlaylistSong? {
        val existingPlaylistSong = playlistSongService.getById(id) ?: return null
        val updatedPlaylistSong = existingPlaylistSong.copy(
            position = position,
        )
        return playlistSongService.update(updatedPlaylistSong)
    }
    
    suspend fun removeSongFromPlaylist(id: UUID): Boolean {
        return playlistSongService.delete(id)
    }
}