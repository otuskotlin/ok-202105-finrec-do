package com.finyou.fintrack.backend.app.ktor

import com.finyou.fintrack.backend.stubs.StubTransactions
import com.finyou.fintrack.openapi.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FinTransactionControllerTest : FunSpec({

    test("init request") {
        val request = InitTransactionRequest(debug = stub)

        withMyTestApplication {
            handleJsonPostRequest("transaction/init", request).apply {
                mapper.readValue(response.content, InitTransactionResponse::class.java).apply {
                    println(this.toString())
                    result shouldBe InitTransactionResponse.Result.SUCCESS
                    errors shouldBe null
                }
            }
        }
    }

    test("create transaction request") {
        val request = CreateTransactionRequest(
            createTransaction = creatableTransaction,
            debug = stub
        )

        withMyTestApplication {
            handleJsonPostRequest("transaction/create", request).apply {
                mapper.readValue(response.content, CreateTransactionResponse::class.java).apply {
                    println(this.toString())
                    result shouldBe CreateTransactionResponse.Result.SUCCESS
                    errors shouldBe null
                    createdTransaction?.id shouldBe StubTransactions.id
                    createdTransaction?.userId shouldBe StubTransactions.userId
                }
            }
        }
    }

    test("read transaction request") {
        val request = ReadTransactionRequest(
            readTransactionId = StubTransactions.id,
            debug = stub
        )

        withMyTestApplication {
            handleJsonPostRequest("transaction/read", request).apply {
                mapper.readValue(response.content, ReadTransactionResponse::class.java).apply {
                    println(this.toString())
                    result shouldBe ReadTransactionResponse.Result.SUCCESS
                    errors shouldBe null
                    readTransaction?.id shouldBe StubTransactions.id
                    readTransaction?.userId shouldBe StubTransactions.userId
                }
            }
        }
    }

    test("update transaction request") {
        val request = UpdateTransactionRequest(
            updateTransaction = updatableTransaction,
            debug = stub
        )

        withMyTestApplication {
            handleJsonPostRequest("transaction/update", request).apply {
                mapper.readValue(response.content, UpdateTransactionResponse::class.java).apply {
                    println(this.toString())
                    result shouldBe UpdateTransactionResponse.Result.SUCCESS
                    errors shouldBe null
                    updatedTransaction?.id shouldBe StubTransactions.id
                    updatedTransaction?.userId shouldBe StubTransactions.userId
                }
            }
        }
    }

    test("delete transaction request") {
        val request = DeleteTransactionRequest(
            deleteTransactionId = StubTransactions.id,
            debug = stub
        )

        withMyTestApplication {
            handleJsonPostRequest("transaction/delete", request).apply {
                mapper.readValue(response.content, DeleteTransactionResponse::class.java).apply {
                    println(this.toString())
                    result shouldBe DeleteTransactionResponse.Result.SUCCESS
                    errors shouldBe null
                    deletedTransaction?.id shouldBe StubTransactions.id
                    deletedTransaction?.userId shouldBe StubTransactions.userId
                }
            }
        }
    }

    test("search transaction request") {
        val request = SearchTransactionRequest(
            pagination = PaginatedRequest(lastId = StubTransactions.id),
            debug = stub,
        )

        withMyTestApplication {
            handleJsonPostRequest("transaction/search", request).apply {
                mapper.readValue(response.content, SearchTransactionResponse::class.java).apply {
                    println(this.toString())
                    result shouldBe SearchTransactionResponse.Result.SUCCESS
                    errors shouldBe null
                    pagination?.lastId shouldBe StubTransactions.id
                    foundItems?.isNotEmpty() shouldBe true
                }
            }
        }
    }

})

val creatableTransaction = CreatableTransaction(
    userId = StubTransactions.userId,
    name = "TV",
    description = "TV Samsung 55\"",
    date = "2021-08-22T15:10:00Z",
    transactionType = TransactionType.OUTCOME,
    amount = 1200.0,
    currency = "USD"
)

val updatableTransaction = UpdatableTransaction(
    id = StubTransactions.id,
    userId = StubTransactions.userId,
    name = "TV",
    description = "TV Samsung 55\"",
    date = "2021-08-22T15:10:00Z",
    transactionType = TransactionType.OUTCOME,
    amount = 1200.0,
    currency = "USD"
)

val stub = Debug(mode = Debug.Mode.STUB, stubCase = Debug.StubCase.SUCCESS)