package com.finyou.fintrack.backend.logic

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionIdModel
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.common.models.StubCaseModel
import com.finyou.fintrack.backend.common.models.UserIdModel
import com.finyou.fintrack.backend.stubs.StubTransactions
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

class FinCrudValidationTest : StringSpec({

    val crud = FinTransactionCrud()
    val transaction = StubTransactions.getStub {
        id = FinTransactionIdModel.NONE
    }


    "CREATE validation success" {
        val context = FtContext(
            stubCase = StubCaseModel.SUCCESS,
            requestTransaction = transaction,
            operation = FinTransactionOperation.CREATE
        )
        runBlocking {
            crud.create(context)
        }
        context.status shouldBe CorStatus.SUCCESS
        context.errors.size shouldBe 0
    }


    "CREATE validation failed" {
        transaction.userId = UserIdModel.NONE
        val context = FtContext(
            stubCase = StubCaseModel.SUCCESS,
            requestTransaction = transaction,
            operation = FinTransactionOperation.CREATE
        )
        runBlocking {
            crud.create(context)
        }
        context.status shouldBe CorStatus.ERROR
        context.errors.size shouldBe 1
    }
})