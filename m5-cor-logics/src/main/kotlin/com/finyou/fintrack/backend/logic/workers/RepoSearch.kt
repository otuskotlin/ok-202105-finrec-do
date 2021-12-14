package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker

internal fun ICorChainDsl<FtContext>.repoSearch(title: String) = worker {
    this.title = title
    description = """
            Search for Ads those are most appropriate to the request criteria
        """.trimIndent()

    on { status == CorStatus.RUNNING }

    handle {
        val result = finTransactionRepo.search(dbSearchFilter)
        if (result.isSuccess) {
            responseTransactions = result.result.toMutableList()
        } else {
            result.errors.forEach {
                addError(it)
            }
        }
    }
}