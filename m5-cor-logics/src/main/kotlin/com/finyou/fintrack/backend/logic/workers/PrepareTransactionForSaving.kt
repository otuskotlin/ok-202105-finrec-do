package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker

fun ICorChainDsl<FtContext>.prepareAdForSaving(title: String) {
    worker {
        this.title = title
        description = title
        on { status == CorStatus.RUNNING }
        handle {
            with(dbTransaction) {
                this.name = requestTransaction.name
                description = requestTransaction.description
                date = requestTransaction.date
                transactionType = requestTransaction.transactionType
                amountCurrency = requestTransaction.amountCurrency
            }
        }
    }
}