package com.finyou.fintrack.backend.app.ktor

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.finyou.fintrack.backend.stubs.StubTransactions
import com.finyou.fintrack.openapi.models.BaseMessage
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*
import java.util.*

val jsonType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
val mapper = jacksonObjectMapper()

fun <R> withMyTestApplication(test: TestApplicationEngine.() -> R) =
    withTestApplication({ module() }, test)

fun TestApplicationEngine.handleJsonPostRequest(
    uri: String,
    body: BaseMessage
): TestApplicationCall = handleRequest(HttpMethod.Post, uri) {
    addContentTypeHeader()
    setBody(mapper.writeValueAsString(body))
}.apply {
    response.status() shouldBe HttpStatusCode.OK
    response.contentType() shouldBe jsonType
}

fun TestApplicationRequest.addContentTypeHeader() = this.apply {
    addHeader(HttpHeaders.ContentType, jsonType.toString())
}

fun KtorAuthConfig.Companion.testUserToken(): String = testToken("TEST", "USER")
fun KtorAuthConfig.Companion.testModerToken(): String = testToken("TEST", "USER", "MODERATOR")
fun KtorAuthConfig.Companion.testAdminToken(): String = testToken("TEST", "USER", "ADMIN")

private fun testToken(vararg groups: String) = JWT.create()
    .withExpiresAt(
        GregorianCalendar().apply {
            set(2036, 0, 1, 0, 0, 0)
            timeZone = TimeZone.getTimeZone("UTC")
        }.time
    )
    .withAudience(KtorAuthConfig.TEST.audience)
    .withIssuer(KtorAuthConfig.TEST.issuer)
    .withClaim(KtorAuthConfig.ID_CLAIM, StubTransactions.userId)
    .withClaim(KtorAuthConfig.F_NAME_CLAIM, "Ivan")
    .withClaim(KtorAuthConfig.M_NAME_CLAIM, "I.")
    .withClaim(KtorAuthConfig.L_NAME_CLAIM, "Ivanov")
    .withArrayClaim(KtorAuthConfig.GROUPS_CLAIM, groups)
    .sign(Algorithm.HMAC256(KtorAuthConfig.TEST.secret))
    .apply { println("Test JWT token: $this") }