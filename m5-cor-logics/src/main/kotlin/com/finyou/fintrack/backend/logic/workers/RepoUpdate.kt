package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker
import com.finyou.fintrack.backend.repo.common.DbFinTransactionModelRequest

internal fun ICorChainDsl<FtContext>.repoUpdate(title: String) = worker {
    this.title = title
    description = "Data from request updates the DB Repository object"

    on { status == CorStatus.RUNNING }

    handle {
        val result = finTransactionRepo.update(DbFinTransactionModelRequest(finTransaction = requestTransaction))
        val resultValue = result.result
        if (result.isSuccess && resultValue != null) {
            responseTransaction =  resultValue
        } else {
            result.errors.forEach {
                addError(it)
            }
        }
    }
}