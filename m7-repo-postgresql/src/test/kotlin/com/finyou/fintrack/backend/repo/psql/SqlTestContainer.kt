package com.finyou.fintrack.backend.repo.psql

import com.finyou.fintrack.backend.common.models.FinTransactionModel
import org.testcontainers.containers.PostgreSQLContainer

class PSQLContainer : PostgreSQLContainer<PSQLContainer>("postgres:13.2")

object SqlTestContainer {
    private const val USER = "fintrack"
    private const val PASSWORD = "finyou"
    private const val SCHEMA = "fintrack"

    private val container by lazy {
        PSQLContainer().apply {
            withUsername(USER)
            withPassword(PASSWORD)
            withDatabaseName(SCHEMA)
            withStartupTimeoutSeconds(300)
            start()
        }
    }

    private val url : String by lazy { container.jdbcUrl }

    fun repoWithTestContainer(initObjects: List<FinTransactionModel>): RepoFinTransactionPSQL {
        return RepoFinTransactionPSQL(url, USER, PASSWORD, SCHEMA, initObjects)
    }
}