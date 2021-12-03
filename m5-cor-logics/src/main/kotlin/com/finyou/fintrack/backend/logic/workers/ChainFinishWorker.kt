package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.cor.common.handlers.worker

internal fun ICorChainDsl<FtContext>.chainFinishWorker(title: String) = chain{
    this.title = title
    description = "Chain will be successful if have no errors"
    worker {
        this.title = "Chain success handler"
        on { status in setOf(CorStatus.RUNNING, CorStatus.FINISHING) }
        handle { status = CorStatus.SUCCESS }
    }
    worker {
        this.title = "Chain not success handler"
        on { status != CorStatus.SUCCESS }
        handle { status = CorStatus.ERROR }
    }
}