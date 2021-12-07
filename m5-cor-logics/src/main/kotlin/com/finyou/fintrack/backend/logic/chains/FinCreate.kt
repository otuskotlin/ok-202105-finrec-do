package com.finyou.fintrack.backend.logic.chains

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.cor.common.cor.ICorExec
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.logic.chains.helpers.onValidationErrorHandle
import com.finyou.fintrack.backend.logic.chains.stubs.finCreateStub
import com.finyou.fintrack.backend.logic.workers.chainFinishWorker
import com.finyou.fintrack.backend.logic.workers.chainInitWorker
import com.finyou.fintrack.backend.logic.workers.checkOperationWorker
import com.finyou.fintrack.backend.validation.cor.workers.validation
import com.finyou.fintrack.backend.validation.validators.ValidatorAmountPositive
import com.finyou.fintrack.backend.validation.validators.ValidatorStringNotEmpty
import com.finyou.fintrack.backend.validation.validators.ValidatorTransactionTime
import java.math.BigDecimal
import java.time.Instant

internal object FinCreate: ICorExec<FtContext> by chain<FtContext>({
    checkOperationWorker(
        title = "Operation check",
        targetOperation = FinTransactionOperation.CREATE,
    )
    chainInitWorker(title = "Chain init")
    validation {
        errorHandler { this.onValidationErrorHandle(it) }
        validate<String?> {
            on { this.requestTransaction.userId.id }
            validator(ValidatorStringNotEmpty(field = "userId"))
        }
        validate<String?> {
            on { this.requestTransaction.name }
            validator(ValidatorStringNotEmpty(field = "transactionName"))
        }
        validate<Instant?> {
            on { this.requestTransaction.date }
            validator(ValidatorTransactionTime(field = "date"))
        }
        validate<BigDecimal?> {
            on { this.requestTransaction.amountCurrency.amount }
            validator(ValidatorAmountPositive(field = "transactionAmount"))
        }
        validate<String?> {
            on { this.requestTransaction.amountCurrency.currency }
            validator(ValidatorStringNotEmpty(field = "transactionCurrency"))
        }
    }

    finCreateStub(title = "CREATE stubCase handling")

    // TODO: Business logic, DB

    chainFinishWorker(title = "Chain finishing")
}).build()