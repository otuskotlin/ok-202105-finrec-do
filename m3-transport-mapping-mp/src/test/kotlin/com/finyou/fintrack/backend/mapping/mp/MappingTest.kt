package com.finyou.fintrack.backend.mapping.mp

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.kmp.models.CreatableTransaction
import com.finyou.fintrack.kmp.models.CreateTransactionRequest
import com.finyou.fintrack.kmp.models.TransactionType
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.time.Instant

class MappingTest : ShouldSpec({

    val query = CreateTransactionRequest(
        requestId = "123456",
        createTransaction = CreatableTransaction(
            userId = "987654",
            name = "Coffee",
            date = "2021-08-22T15:10:00Z",
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
        context.requestTransaction.amountCurrency shouldBe AmountCurrencyModel(BigDecimal.valueOf(2.75),"USD")
        context.requestTransaction.date shouldBe Instant.parse("2021-08-22T15:10:00Z")
    }

    val context = FtContext(
        onRequest = "7474",
        responseTransaction = FinTransactionModel(
            id = FinTransactionIdModel("91009"),
            userId = UserIdModel("987654"),
            name = "Coffee",
            date = Instant.parse("2021-08-22T00:00:00Z"),
            transactionType = TypeModel.OUTCOME,
            amountCurrency = AmountCurrencyModel(BigDecimal.valueOf(2.75), "USD"),
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
        response.createdTransaction?.date shouldBe "2021-08-22T00:00:00Z"
        response.createdTransaction?.permissions?.size shouldBe 2
    }
})