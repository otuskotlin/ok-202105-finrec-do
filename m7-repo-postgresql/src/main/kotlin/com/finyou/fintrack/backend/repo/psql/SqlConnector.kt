package com.finyou.fintrack.backend.repo.psql

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.DEFAULT_ISOLATION_LEVEL
import org.jetbrains.exposed.sql.transactions.transaction

class SqlConnector(
    private val url: String,
    private val user: String,
    private val password: String,
    private val schema: String,
    private val databaseConfig: DatabaseConfig = DatabaseConfig {
        defaultIsolationLevel = DEFAULT_ISOLATION_LEVEL
    }
) {
    enum class DbType(val driver: String) {
        PostgreSQL("org.postgresql.Driver")
    }

    private val dbType: DbType = when {
        url.startsWith("jdbc:postgresql://") -> DbType.PostgreSQL
        else -> throw IllegalArgumentException("Incorrect url")
    }

    private val db = Database.connect(url, dbType.driver, user, password, databaseConfig = databaseConfig)

    fun connect(vararg tables: Table): Database {
        transaction(db) {
            when (dbType) {
                DbType.PostgreSQL -> {
                    connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS $schema", false).executeUpdate()
                }
            }
        }

        val connect = Database.connect(
            url, dbType.driver, user, password,
            databaseConfig = databaseConfig,
            setupConnection = { connection ->
                when (dbType) {
                    DbType.PostgreSQL -> {
                        connection.transactionIsolation = DEFAULT_ISOLATION_LEVEL
                        connection.schema = schema
                    }
                }
            }
        )

        transaction(connect) {
            if (System.getenv("com.finyou.sql_drop_db")?.toBoolean() == true) {
                SchemaUtils.drop(*tables, inBatch = true)
                SchemaUtils.create(*tables, inBatch = true)
            } else if (System.getenv("com.finyou.sql_fast_migration").toBoolean()) {
                // TODO: Place to exec migration: create and ensure tables
            } else {
                SchemaUtils.createMissingTablesAndColumns(*tables, inBatch = true)
            }
        }

        return connect
    }
}