package com.fugisawa.playlistsgql

import com.fugisawa.playlistsgql.infrastructure.database.configureDatabases
import com.fugisawa.playlistsgql.infrastructure.di.appModules
import com.fugisawa.playlistsgql.infrastructure.graphql.configureGraphQL
import com.fugisawa.playlistsgql.infrastructure.security.JwtConfig
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(appModules)
    }

    JwtConfig.initialize(environment.config)

    configureDatabases()
    configureRouting()
    configureGraphQL()
}
