package com.finyou.fintrack.backend.app.ktor

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

class ApplicationTest : FunSpec ({

    test("root") {
        withMyTestApplication {
            handleRequest(HttpMethod.Get, "/").apply {
                response.status() shouldBe HttpStatusCode.OK
                response.content shouldBe "Hello World!"
            }
        }
    }
})