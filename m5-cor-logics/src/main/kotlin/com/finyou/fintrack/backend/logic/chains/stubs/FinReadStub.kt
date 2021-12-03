package com.finyou.fintrack.backend.logic.chains.stubs

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.stubs.StubTransactions

internal fun ICorChainDsl<FtContext>.finReadStub(
    title: String
) = finStub(title, FinTransactionOperation.READ) {
    responseTransaction = StubTransactions.getStub {
        id = requestTransactionId
    }
    status = CorStatus.FINISHING
}