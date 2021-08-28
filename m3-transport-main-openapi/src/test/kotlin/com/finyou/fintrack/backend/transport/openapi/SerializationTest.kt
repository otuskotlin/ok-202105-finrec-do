package com.finyou.fintrack.backend.transport.openapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.finyou.fintrack.openapi.models.BaseMessage
import com.finyou.fintrack.openapi.models.CreatableTransaction
import com.finyou.fintrack.openapi.models.CreateTransactionRequest
import com.finyou.fintrack.openapi.models.TransactionType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class SerializationTest : StringSpec({

    val requestId = "123456"
    val createTransactionRequest = CreateTransactionRequest(
        requestId = requestId,
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

    val om = ObjectMapper()

    val json = om.writeValueAsString(createTransactionRequest)

    "json must contain discriminator" {
        json shouldContain """"messageType":"${createTransactionRequest::class.simpleName}""""
    }

    "json must serialize TransactionType field" {
        json shouldContain """"transactionType":"${TransactionType.OUTCOME.value}""""
    }

    "json must serialize messageId field" {
        json shouldContain """"requestId":"$requestId""""
    }

    val deserialized = om.readValue(json, BaseMessage::class.java) as CreateTransactionRequest

    "deserialized request must deserialize TransactionType field" {
        deserialized.createTransaction?.transactionType shouldBe TransactionType.OUTCOME
    }

    "deserialized request must deserialize messageId field" {
        deserialized.requestId shouldBe requestId
    }
})