package com.fugisawa.playlistsgql.infrastructure.config.database

import com.fugisawa.playlistsgql.data.dao.PlaylistSongTable
import com.fugisawa.playlistsgql.data.dao.PlaylistTable
import com.fugisawa.playlistsgql.data.dao.SongTable
import com.fugisawa.playlistsgql.data.dao.UserTable
import com.fugisawa.playlistsgql.data.dao.VoteTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSchema {
    fun createSchema() {
        transaction {
            SchemaUtils.create(
                UserTable,
                SongTable,
                PlaylistTable,
                PlaylistSongTable,
                VoteTable,
            )
        }
    }
}
