package com.finyou.fintrack.backend.mapping.openapi

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.openapi.models.CreatableTransaction
import com.finyou.fintrack.openapi.models.CreateTransactionRequest
import com.finyou.fintrack.openapi.models.TransactionType
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class MappingTest : ShouldSpec({

    val query = CreateTransactionRequest(
        requestId = "123456",
        createTransaction = CreatableTransaction(
            userId = "987654",
            name = "Coffee",
            date = "22.08.2021",
            time = "15:10",
            transactionType = TransactionType.OUTCOME,
            amount = 2.75,
            currency = "USD"
        )
    )

    should("check mapping to context") {
        val context = FtContext().setQuery(query)
        context.onRequest shouldBe "123456"
        context.requestTransaction.userId shouldBe UserIdModel("987654")
        context.requestTransaction.name shouldBe "Coffee"
        context.requestTransaction.transactionType shouldBe TypeModel.OUTCOME
        context.requestTransaction.amount shouldBe 2.75
        context.requestTransaction.currency shouldBe CurrencyModel("USD")
        context.requestTransaction.date shouldBe convertCreateDate(query.createTransaction?.date, query.createTransaction?.time)
    }

    val context = FtContext(
        onRequest = "7474",
        responseTransaction = TransactionModel(
            id = TransactionIdModel("91009"),
            userId = UserIdModel("987654"),
            name = "Coffee",
            date = 1629645000000,
            transactionType = TypeModel.OUTCOME,
            amount = 2.75,
            currency = CurrencyModel("USD"),
            permissions = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE)
        )
    )


    should("check mapping to transport") {
        val response = context.toCreateResponse()
        response.requestId shouldBe "7474"
        response.createdTransaction?.id shouldBe "91009"
        response.createdTransaction?.userId shouldBe "987654"
        response.createdTransaction?.name shouldBe "Coffee"
        response.createdTransaction?.transactionType shouldBe TransactionType.OUTCOME
        response.createdTransaction?.amount shouldBe 2.75
        response.createdTransaction?.currency shouldBe "USD"
        response.createdTransaction?.date shouldBe "22.08.2021"
        response.createdTransaction?.permissions?.size shouldBe 2
    }
})