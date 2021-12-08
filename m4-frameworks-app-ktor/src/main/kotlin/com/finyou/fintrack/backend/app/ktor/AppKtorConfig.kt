package com.finyou.fintrack.backend.app.ktor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.finyou.fintrack.backend.app.ktor.models.KtorUserSession
import com.finyou.fintrack.backend.app.ktor.services.FinTransactionService
import com.finyou.fintrack.backend.common.context.ContextConfig
import com.finyou.fintrack.backend.logic.FinTransactionCrud
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction
import com.finyou.fintrack.backend.repo.inmemory.RepoFinTransactionInMemory
import java.time.Duration

data class AppKtorConfig(
    val userSessions: MutableSet<KtorUserSession> = mutableSetOf<KtorUserSession>(),
    val objectMapper: ObjectMapper = jacksonObjectMapper(),
    val finTransactionRepoTest: IRepoFinTransaction = RepoFinTransactionInMemory(initObjects = listOf()),
    val finTransactionRepoProd: IRepoFinTransaction = RepoFinTransactionInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
    val contextConfig: ContextConfig = ContextConfig(
        repoProd = finTransactionRepoProd,
        repoTest = finTransactionRepoTest,
    ),
    val crud: FinTransactionCrud = FinTransactionCrud(contextConfig),
    val finTransactionService: FinTransactionService = FinTransactionService(crud),
)
