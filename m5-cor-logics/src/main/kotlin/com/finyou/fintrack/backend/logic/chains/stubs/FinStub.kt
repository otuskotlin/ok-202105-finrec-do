package com.finyou.fintrack.backend.logic.chains.stubs

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.ErrorModel
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.common.models.StubCaseModel
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.cor.common.handlers.worker

internal fun ICorChainDsl<FtContext>.finStub(
    title: String,
    operation: FinTransactionOperation,
    handleOperationStub: suspend FtContext.() -> Unit
) = chain {
    this.title = title
    on { status == CorStatus.RUNNING && stubCase != StubCaseModel.NONE }

    worker {
        this.title = "Success stub for ${operation.name}"
        on { stubCase == StubCaseModel.SUCCESS }
        handle(handleOperationStub)
    }

    worker {
        this.title = "No such stub handler"
        on { status == CorStatus.RUNNING }
        handle {
            status = CorStatus.FAILING
            addError(
                error = ErrorModel("No such stub case ${stubCase.name}", "stubCase"),
            )
        }
    }

}