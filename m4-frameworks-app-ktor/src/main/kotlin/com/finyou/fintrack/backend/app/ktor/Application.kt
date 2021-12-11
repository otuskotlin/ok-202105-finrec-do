package com.finyou.fintrack.backend.app.ktor

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.finyou.fintrack.backend.app.ktor.controllers.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*

fun main(args: Array<String>) {
    val appEnvironment = commandLineEnvironment(args).config.config("ktor.deployment")
    embeddedServer(Netty,
        port = appEnvironment.propertyOrNull("port")?.getString()?.toIntOrNull() ?: 8080,
        host = appEnvironment.propertyOrNull("host")?.getString() ?: "127.0.0.1"
    ) {
        module()
    }.start(wait = true)
}

fun Application.module(config: AppKtorConfig = AppKtorConfig()) {
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

    val service = config.finTransactionService
    val objectMapper = config.objectMapper
    val wsUserSessions = config.userSessions

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