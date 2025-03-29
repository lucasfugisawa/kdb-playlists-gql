package com.fugisawa.playlistsgql

import com.fugisawa.playlistsgql.data.repositories.configureRepositories
import com.fugisawa.playlistsgql.infrastructure.config.database.configureDatabases
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    configureDatabases()
    configureRepositories()
    configureRouting()
}
