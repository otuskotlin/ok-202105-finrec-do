package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker

internal fun ICorChainDsl<FtContext>.chainInitWorker(title: String) = worker {
    this.title = title
    description = "Check chain status in start, should be NONE"
    on { status == CorStatus.NONE }
    handle { status = CorStatus.RUNNING }
}