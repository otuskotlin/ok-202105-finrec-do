package com.finyou.fintrack.backend.logic.chains.stubs

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.stubs.StubTransactions

internal fun ICorChainDsl<FtContext>.finCreateStub(
    title: String
) = finStub(title, FinTransactionOperation.CREATE) {
    responseTransaction = requestTransaction.copy(
        id = StubTransactions.idModel,
        permissions = StubTransactions.permissionsDefault
    )
    status = CorStatus.FINISHING
}