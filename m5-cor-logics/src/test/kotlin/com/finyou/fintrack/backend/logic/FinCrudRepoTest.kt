package com.finyou.fintrack.backend.logic

import com.finyou.fintrack.backend.common.context.ContextConfig
import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.repo.inmemory.RepoFinTransactionInMemory
import com.finyou.fintrack.backend.stubs.StubTransactions
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.util.*

class FinCrudRepoTest : StringSpec({

    "Create transaction success" {
        val repo = RepoFinTransactionInMemory()
        val crud = FinTransactionCrud(config = ContextConfig(
            repoTest = repo
        ))
        val transaction = StubTransactions.getStub {
            id = FinTransactionIdModel.NONE
        }
        val context = FtContext(
            appMode = AppModeModel.TEST,
            requestTransaction = transaction,
            operation = FinTransactionOperation.CREATE,
            principal = principalUser()
        )
        runBlocking {
            crud.create(context)
        }
        println("errors = ${context.errors}")
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id.toString().isNotBlank().shouldBeTrue()
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
        val idModel = FinTransactionIdModel(UUID.randomUUID().toString())
        val repo = RepoFinTransactionInMemory(
            initObjects = listOf(StubTransactions.getStub { id = idModel })
        )
        val crud = FinTransactionCrud(config = ContextConfig(
            repoTest = repo
        ))
        val context = FtContext(
            appMode = AppModeModel.TEST,
            requestTransactionId = idModel,
            operation = FinTransactionOperation.READ,
            principal = principalUser()
        )
        runBlocking {
            crud.read(context)
        }
        println("errors = ${context.errors}")
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id shouldBe idModel
            userId shouldBe StubTransactions.userIdModel
        }
    }

    "UPDATE success" {
        val idModel = FinTransactionIdModel(UUID.randomUUID().toString())
        val initTransaction = StubTransactions.getStub { id = idModel }
        val repo = RepoFinTransactionInMemory(
            initObjects = listOf(initTransaction)
        )
        val crud = FinTransactionCrud(config = ContextConfig(
            repoTest = repo
        ))
        val newTransactionType = TypeModel.OUTCOME
        val newAmountCurrency = AmountCurrencyModel(
            BigDecimal.valueOf(55.23), "USD")
        val updateTransaction = initTransaction.copy(
            transactionType = newTransactionType,
            amountCurrency = newAmountCurrency
        )
        val context = FtContext(
            appMode = AppModeModel.TEST,
            requestTransaction = updateTransaction,
            operation = FinTransactionOperation.UPDATE,
            principal = principalUser()
        )
        runBlocking {
            crud.update(context)
        }
        println("errors = ${context.errors}")
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id shouldBe idModel
            userId shouldBe updateTransaction.userId
            transactionType shouldBe newTransactionType
            amountCurrency shouldBe newAmountCurrency
            permissions shouldBe StubTransactions.permissionsDefault
        }
    }

    "DELETE success" {
        val idModel = FinTransactionIdModel(UUID.randomUUID().toString())
        val repo = RepoFinTransactionInMemory(
            initObjects = listOf(StubTransactions.getStub{id = idModel})
        )
        val crud = FinTransactionCrud(config = ContextConfig(
            repoTest = repo
        ))
        val context = FtContext(
            appMode = AppModeModel.TEST,
            requestTransactionId = idModel,
            operation = FinTransactionOperation.DELETE,
            principal = principalUser()
        )
        runBlocking {
            crud.delete(context)
        }
        println("errors = ${context.errors}")
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransaction) {
            id shouldBe idModel
            userId shouldBe StubTransactions.userIdModel
        }
    }

    "SEARCH success" {
        val transaction = StubTransactions.getStub()
        val repo = RepoFinTransactionInMemory(
            initObjects = listOf(
                transaction.copy(
                    id = FinTransactionIdModel("123"),
                    transactionType = TypeModel.OUTCOME
                ),
                transaction.copy(
                    id = FinTransactionIdModel("234"),
                    transactionType = TypeModel.INCOME
                ),
                transaction.copy(
                    id = FinTransactionIdModel("2345"),
                    transactionType = TypeModel.OUTCOME
                ),
                transaction.copy(
                    id = FinTransactionIdModel("23435"),
                    transactionType = TypeModel.INCOME
                ),
            )
        )
        val crud = FinTransactionCrud(config = ContextConfig(
            repoTest = repo
        ))
        val context = FtContext(
            appMode = AppModeModel.TEST,
            requestPage = PaginatedRequestModel(lastId = FinTransactionIdModel("123")),
            searchFilter = FilterModel(transactionType = TypeModel.INCOME),
            operation = FinTransactionOperation.SEARCH,
            principal = principalUser()
        )
        runBlocking {
            crud.search(context)
        }
        println("errors = ${context.errors}")
        context.status shouldBe CorStatus.SUCCESS
        with(context.responseTransactions) {
            size shouldBe 2
        }
    }
})