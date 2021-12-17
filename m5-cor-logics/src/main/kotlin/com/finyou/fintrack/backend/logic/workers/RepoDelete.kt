package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker
import com.finyou.fintrack.backend.repo.common.DbFinTransactionIdRequest

internal fun ICorChainDsl<FtContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "The requested object will be deleted from the DB Repository"

    on { status == CorStatus.RUNNING }

    handle {
        val result = finTransactionRepo.delete(DbFinTransactionIdRequest(id = dbTransaction.id))
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