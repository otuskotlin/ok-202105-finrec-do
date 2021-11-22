package com.finyou.fintrack.backend.logic.chains.stubs

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.common.models.PaginatedResponseModel
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.stubs.StubTransactions

internal fun ICorChainDsl<FtContext>.finSearchStub(
    title: String
) = finStub(title, FinTransactionOperation.SEARCH) {
    responseTransactions = StubTransactions.getStubs()
    responsePage = PaginatedResponseModel(lastId = requestPage.lastId)
    status = CorStatus.FINISHING
}