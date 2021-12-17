package com.finyou.fintrack.backend.logic.chains

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.cor.common.cor.ICorExec
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.logic.chains.helpers.onValidationErrorHandle
import com.finyou.fintrack.backend.logic.chains.stubs.finDeleteStub
import com.finyou.fintrack.backend.logic.workers.*
import com.finyou.fintrack.backend.logic.workers.chainFinishWorker
import com.finyou.fintrack.backend.logic.workers.chainInitWorker
import com.finyou.fintrack.backend.logic.workers.checkOperationWorker
import com.finyou.fintrack.backend.logic.workers.chooseDb
import com.finyou.fintrack.backend.validation.cor.workers.validation
import com.finyou.fintrack.backend.validation.validators.ValidatorStringNotEmpty

internal object FinDelete: ICorExec<FtContext> by chain<FtContext>({
    checkOperationWorker(
        title = "Operation check",
        targetOperation = FinTransactionOperation.DELETE,
    )
    chainInitWorker(title = "Chain init")
    chooseDb(title = "Choose DB or stub")
    validation {
        errorHandler { this.onValidationErrorHandle(it) }
        validate<String?> {
            on { this.requestTransactionId.toString() }
            validator(ValidatorStringNotEmpty(field = "requestTransactionId"))
        }
    }

    finDeleteStub(title = "DELETE stubCase handling")
    chainPermissions("Вычисление разрешений для пользователя")
    repoRead(title = "Read object from DB")
    accessValidation("Вычисление прав доступа")
    repoDelete(title = "Delete object from DB")

    chainFinishWorker(title = "Chain finishing")
}).build()