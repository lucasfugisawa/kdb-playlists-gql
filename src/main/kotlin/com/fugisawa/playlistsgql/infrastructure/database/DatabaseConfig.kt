package com.fugisawa.playlistsgql.infrastructure.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import javax.sql.DataSource

class DatabaseConfig {
    private lateinit var dataSource: DataSource

    fun init(config: ApplicationConfig) {
        val driver = config.property("database.driver").getString()
        val url = config.property("database.url").getString()
        val user = config.property("database.user").getString()
        val password = config.property("database.password").getString()

        dataSource = hikari(url, driver, user, password)
        Database.connect(dataSource)
        runFlyway(url, user, password)
    }

    private fun hikari(
        url: String,
        driver: String,
        user: String,
        password: String,
    ): HikariDataSource {
        val config =
            HikariConfig().apply {
                driverClassName = driver
                jdbcUrl = url
                username = user
                this.password = password
                maximumPoolSize = 10
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }
        return HikariDataSource(config)
    }

    private fun runFlyway(
        url: String,
        user: String,
        password: String,
    ) {
        val flyway =
            Flyway
                .configure()
                .dataSource(url, user, password)
                .locations("classpath:db/migration")
                .load()
        flyway.migrate()
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}

fun Application.configureDatabases() {
    val databaseConfig = environment.config
    val dbConfig = DatabaseConfig()
    dbConfig.init(databaseConfig)
    log.info("Database initialized")
}
