package com.finyou.fintrack.backend.app.ktor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.finyou.fintrack.openapi.models.BaseMessage
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

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