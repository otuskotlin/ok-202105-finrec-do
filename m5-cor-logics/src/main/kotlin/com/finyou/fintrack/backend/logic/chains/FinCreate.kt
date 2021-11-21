package com.finyou.fintrack.backend.logic.chains

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.cor.common.cor.ICorExec
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.logic.chains.stubs.finCreateStub
import com.finyou.fintrack.backend.logic.workers.chainFinishWorker
import com.finyou.fintrack.backend.logic.workers.chainInitWorker
import com.finyou.fintrack.backend.logic.workers.checkOperationWorker

internal object FinCreate: ICorExec<FtContext> by chain<FtContext>({
    checkOperationWorker(
        title = "Operation check",
        targetOperation = FinTransactionOperation.CREATE,
    )
    chainInitWorker(title = "Chain init")
    // TODO: Validation

    finCreateStub(title = "CREATE stubCase handling")

    // TODO: Business logic, DB

    chainFinishWorker(title = "Chain finishing")
}).build()