package com.finyou.fintrack.backend.app.ktor

import com.finyou.fintrack.openapi.models.BaseMessage
import io.kotest.core.test.TestContext
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

inline fun <reified T> TestContext.testPostRequest(
        body: BaseMessage? = null,
        uri: String,
        config: AppKtorConfig = AppKtorConfig(),
        result: HttpStatusCode = HttpStatusCode.OK,
        crossinline block: T.() -> Unit = {}
    ) {
        withTestApplication({
            module(config = config)
        }) {
            handleRequest(HttpMethod.Post, uri) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                addHeader(HttpHeaders.Authorization, "Bearer ${KtorAuthConfig.testUserToken()}")
                setBody(mapper.writeValueAsString(body))
            }.apply {
                println(response.content)
                response.status() shouldBe result
                if (result == HttpStatusCode.OK) {
                    response.contentType() shouldBe ContentType.Application.Json.withCharset(Charsets.UTF_8)
                    mapper.readValue(response.content, T::class.java).block()
                }
            }
        }
    }