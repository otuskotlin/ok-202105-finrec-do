package com.finyou.fintrack.backend.logic.chains

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.cor.common.cor.ICorExec
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.logic.chains.helpers.onValidationErrorHandle
import com.finyou.fintrack.backend.logic.chains.stubs.finSearchStub
import com.finyou.fintrack.backend.logic.workers.*
import com.finyou.fintrack.backend.logic.workers.chainFinishWorker
import com.finyou.fintrack.backend.logic.workers.chainInitWorker
import com.finyou.fintrack.backend.logic.workers.checkOperationWorker
import com.finyou.fintrack.backend.logic.workers.chooseDb
import com.finyou.fintrack.backend.validation.cor.workers.validation
import com.finyou.fintrack.backend.validation.validators.ValidatorStringNotEmpty

internal object FinSearch: ICorExec<FtContext> by chain<FtContext>({
    checkOperationWorker(
        title = "Operation check",
        targetOperation = FinTransactionOperation.SEARCH,
    )
    chainInitWorker(title = "Chain init")
    chooseDb(title = "Choose DB or stub")
    validation {
        errorHandler { this.onValidationErrorHandle(it) }
        validate<String?> {
            on { this.requestPage.lastId.id }
            validator(ValidatorStringNotEmpty(field = "pagination.lastId"))
        }
    }

    finSearchStub(title = "SEARCH stubCase handling")

    repoSearch(title = "Search objects in DB")

    chainFinishWorker(title = "Chain finishing")
}).build()