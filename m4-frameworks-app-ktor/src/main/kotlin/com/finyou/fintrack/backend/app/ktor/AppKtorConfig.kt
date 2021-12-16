package com.finyou.fintrack.backend.app.ktor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.finyou.fintrack.backend.app.ktor.models.KtorUserSession
import com.finyou.fintrack.backend.app.ktor.services.FinTransactionService
import com.finyou.fintrack.backend.common.context.ContextConfig
import com.finyou.fintrack.backend.logic.FinTransactionCrud
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction
import com.finyou.fintrack.backend.repo.inmemory.RepoFinTransactionInMemory
import com.finyou.fintrack.backend.repo.psql.RepoFinTransactionPSQL

data class AppKtorConfig(
    val userSessions: MutableSet<KtorUserSession> = mutableSetOf<KtorUserSession>(),
    val objectMapper: ObjectMapper = jacksonObjectMapper(),
    val finTransactionRepoTest: IRepoFinTransaction = RepoFinTransactionInMemory(initObjects = listOf()),
    val finTransactionRepoProd: IRepoFinTransaction = RepoFinTransactionPSQL(),
    val contextConfig: ContextConfig = ContextConfig(
        repoProd = finTransactionRepoProd,
        repoTest = finTransactionRepoTest,
    ),
    val crud: FinTransactionCrud = FinTransactionCrud(contextConfig),
    val finTransactionService: FinTransactionService = FinTransactionService(crud),
    val auth: KtorAuthConfig = KtorAuthConfig.TEST,
)
