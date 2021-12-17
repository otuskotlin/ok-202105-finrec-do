package com.finyou.fintrack.backend.app.ktor.auth

import com.finyou.fintrack.backend.app.ktor.AppKtorConfig
import com.finyou.fintrack.backend.app.ktor.KtorAuthConfig
import com.finyou.fintrack.backend.app.ktor.testPostRequest
import com.finyou.fintrack.backend.mapping.openapi.transport
import com.finyou.fintrack.backend.stubs.StubTransactions
import com.finyou.fintrack.openapi.models.Debug
import com.finyou.fintrack.openapi.models.ReadTransactionRequest
import com.finyou.fintrack.openapi.models.ReadTransactionResponse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.ktor.http.*

internal class AuthWrongIssuer : FunSpec({

    val transaction = StubTransactions.getStub().copy()

    test("authPositiveTest") {
        val data = ReadTransactionRequest(
            readTransactionId = transaction.id.toString(),
            debug = stub
        )
        testPostRequest<ReadTransactionResponse>(
            body=data,
            uri="/transaction/read",
            config = AppKtorConfig(
                auth = KtorAuthConfig.TEST.copy()
            )
        ) {
            result shouldBe ReadTransactionResponse.Result.SUCCESS
            errors.shouldBeNull()
            readTransaction shouldBe transaction.transport
        }
    }

    test("authWrongIssuerTest") {
        val data = ReadTransactionRequest(
            readTransactionId = transaction.id.toString(),
            debug = stub
        )
        testPostRequest<ReadTransactionResponse>(
            body=data,
            uri="/transaction/read",
            config = AppKtorConfig(
                auth = KtorAuthConfig.TEST.copy(
                    issuer = "some other company"
                )
            ),
            result = HttpStatusCode.Unauthorized,
        )
    }

    test("authWrongSecretTest") {
        val data = ReadTransactionRequest(
            readTransactionId = transaction.id.toString(),
            debug = stub
        )
        testPostRequest<ReadTransactionResponse>(
            body=data,
            uri="/transaction/read",
            config = AppKtorConfig(
                auth = KtorAuthConfig.TEST.copy(
                    secret = "wrong secret"
                )
            ),
            result = HttpStatusCode.Unauthorized,
        )
    }
})

val stub = Debug(mode = Debug.Mode.STUB, stubCase = Debug.StubCase.SUCCESS)