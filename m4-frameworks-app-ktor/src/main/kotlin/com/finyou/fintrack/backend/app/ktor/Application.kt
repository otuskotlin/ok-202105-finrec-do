package com.finyou.fintrack.backend.app.ktor

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.finyou.fintrack.backend.app.ktor.controllers.*
import com.finyou.fintrack.backend.app.ktor.models.KtorUserSession
import com.finyou.fintrack.backend.app.ktor.services.FinTransactionService
import com.finyou.fintrack.backend.logic.FinTransactionCrud
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}

fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(AutoHeadResponse)
    install(Routing)
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            enable(SerializationFeature.INDENT_OUTPUT)
            writerWithDefaultPrettyPrinter()
        }
    }
    install(WebSockets)

    val crud = FinTransactionCrud()
    val service = FinTransactionService(crud)
    val objectMapper = jacksonObjectMapper()
    val wsUserSessions = mutableSetOf<KtorUserSession>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("transaction") {
            post("init") {
                call.init(service)
            }
            post("create") {
                call.createFinTransaction(service)
            }
            post("read") {
                call.readFinTransaction(service)
            }
            post("update") {
                call.updateFinTransaction(service)
            }
            post("delete") {
                call.deleteFinTransaction(service)
            }
            post("search") {
                call.searchFinTransaction(service)
            }
        }
        webSocket("ws") { handleSession(objectMapper, service, wsUserSessions) }
    }
}