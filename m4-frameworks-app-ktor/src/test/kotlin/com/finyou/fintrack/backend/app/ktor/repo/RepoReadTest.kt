package com.finyou.fintrack.backend.app.ktor.repo

import com.fasterxml.jackson.databind.ObjectMapper
import com.finyou.fintrack.backend.app.ktor.AppKtorConfig
import com.finyou.fintrack.backend.app.ktor.mapper
import com.finyou.fintrack.backend.app.ktor.module
import com.finyou.fintrack.backend.common.models.FinTransactionIdModel
import com.finyou.fintrack.backend.repo.inmemory.RepoFinTransactionInMemory
import com.finyou.fintrack.backend.stubs.StubTransactions
import com.finyou.fintrack.openapi.models.Debug
import com.finyou.fintrack.openapi.models.ReadTransactionRequest
import com.finyou.fintrack.openapi.models.ReadTransactionResponse
import com.finyou.fintrack.openapi.models.TransactionType
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.fail
import java.util.*

class RepoReadTest : FunSpec({

    val transaction = StubTransactions.getStub().copy(
        id = FinTransactionIdModel(UUID.randomUUID().toString())
    )

    test("Repo read test") {
        withTestApplication({
            val config = AppKtorConfig(
                finTransactionRepoTest = RepoFinTransactionInMemory(
                    initObjects = listOf(transaction)
                )
            )
            module(config)
        }) {
            handleRequest(HttpMethod.Post, "/transaction/read") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                val request = ReadTransactionRequest(
                    debug = Debug(mode = Debug.Mode.TEST),
                    readTransactionId = transaction.id.toString()
                )
                val json = ObjectMapper().writeValueAsString(request)
                setBody(json)
            }.apply {
                response.status() shouldBe HttpStatusCode.OK
                ContentType.Application.Json.withCharset(Charsets.UTF_8) shouldBe response.contentType()
                val jsonString = response.content ?: fail("Null response json")
                println("|$jsonString|")

                val res = mapper.readValue(response.content, ReadTransactionResponse::class.java)
                    ?: fail("Incorrect response format")

                ReadTransactionResponse.Result.SUCCESS shouldBe res.result
                transaction.id.toString() shouldBe res.readTransaction?.id
                TransactionType.INCOME shouldBe res.readTransaction?.transactionType
                2000.0 shouldBe res.readTransaction?.amount
                transaction.name shouldBe res.readTransaction?.name
                transaction.description shouldBe res.readTransaction?.description
            }
        }
    }
})