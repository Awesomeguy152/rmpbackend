package com.example.plugins

import com.example.schema.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val urlFromEnv = System.getenv("DB_URL")
    val host = System.getenv("DB_HOST") ?: "localhost"
    val port = (System.getenv("DB_PORT") ?: "5433").toInt()
    val db   = System.getenv("DB_NAME") ?: "appdb"
    val user = System.getenv("DB_USER") ?: "app"
    val pass = System.getenv("DB_PASSWORD") ?: "app"

    val jdbcUrl = urlFromEnv ?: "jdbc:postgresql://$host:$port/$db"

    val ds = HikariDataSource(HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        this.jdbcUrl = jdbcUrl
        username = user
        password = pass

        initializationFailTimeout = -1
        connectionTimeout = 15000
        maximumPoolSize = 10
        minimumIdle = 0
    })

    Database.connect(ds)

    transaction {
        SchemaUtils.createMissingTablesAndColumns(UserTable)
    }
}
