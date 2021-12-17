package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker
import com.finyou.fintrack.backend.repo.common.DbFinTransactionModelRequest

internal fun ICorChainDsl<FtContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Data from request are stored in the DB Repository"

    on { status == CorStatus.RUNNING }

    handle {
        val result = finTransactionRepo.create(DbFinTransactionModelRequest(finTransaction = dbTransaction))
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