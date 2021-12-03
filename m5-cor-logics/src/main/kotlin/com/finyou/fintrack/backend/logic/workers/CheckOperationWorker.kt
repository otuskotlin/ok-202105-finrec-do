package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.ErrorModel
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker

internal fun ICorChainDsl<FtContext>.checkOperationWorker(
    targetOperation: FinTransactionOperation,
    title: String
) = worker{
    this.title = title
    description = "Если в контексте недопустимая операция, то чейн неуспешен"
    on { operation != targetOperation }
    handle {
        status = CorStatus.FAILING
        addError(
            error = ErrorModel("Expexted ${targetOperation.name} but was ${operation.name}")
        )
    }
}