package com.finyou.fintrack.backend.logic

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.stubs.StubTransactions
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

class FinCrudTest : StringSpec ({
    val crud = FinTransactionCrud()

    "CREATE success" {
        val transaction = StubTransactions.getStub {
            id = FinTransactionIdModel.NONE
        }
        val context = FtContext(
            stubCase = StubCaseModel.SUCCESS,
            requestTransaction = transaction,
            operation = FinTransactionOperation.CREATE
        )
        runBlocking {
            crud.create(context)
        }
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id shouldBe StubTransactions.idModel
            userId shouldBe transaction.userId
            name shouldBe transaction.name
            description shouldBe transaction.description
            date shouldBe transaction.date
            transactionType shouldBe transaction.transactionType
            amountCurrency shouldBe transaction.amountCurrency
            permissions shouldBe StubTransactions.permissionsDefault
        }
    }

    "READ success" {
        val context = FtContext(
            stubCase = StubCaseModel.SUCCESS,
            requestTransactionId = StubTransactions.idModel,
            operation = FinTransactionOperation.READ
        )
        runBlocking {
            crud.read(context)
        }
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id shouldBe StubTransactions.idModel
            userId shouldBe StubTransactions.userIdModel
        }
    }

    "UPDATE success" {
        val transaction = StubTransactions.getStub()
        val context = FtContext(
            stubCase = StubCaseModel.SUCCESS,
            requestTransaction = transaction,
            operation = FinTransactionOperation.UPDATE
        )
        runBlocking {
            crud.update(context)
        }
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id shouldBe transaction.id
            userId shouldBe transaction.userId
            name shouldBe transaction.name
            description shouldBe transaction.description
            date shouldBe transaction.date
            transactionType shouldBe transaction.transactionType
            amountCurrency shouldBe transaction.amountCurrency
            permissions shouldBe StubTransactions.permissionsDefault
        }
    }

    "DELETE success" {
        val transactionId = FinTransactionIdModel("9871456")
        val context = FtContext(
            stubCase = StubCaseModel.SUCCESS,
            requestTransactionId = transactionId,
            operation = FinTransactionOperation.DELETE
        )
        runBlocking {
            crud.delete(context)
        }
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id shouldBe transactionId
            userId shouldBe StubTransactions.userIdModel
        }
    }

    "SEARCH success" {
        val context = FtContext(
            stubCase = StubCaseModel.SUCCESS,
            requestPage = PaginatedRequestModel(lastId = FinTransactionIdModel("123")),
            searchFilter = FilterModel(),
            operation = FinTransactionOperation.SEARCH
        )
        runBlocking {
            crud.search(context)
        }
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransactions) {
            size shouldBe StubTransactions.getStubs().size
        }
    }
})