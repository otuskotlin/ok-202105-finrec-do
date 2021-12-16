package com.finyou.fintrack.backend.app.ktor

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.finyou.fintrack.backend.app.ktor.KtorAuthConfig.Companion.GROUPS_CLAIM
import com.finyou.fintrack.backend.app.ktor.controllers.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.websocket.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(config: AppKtorConfig = AppKtorConfig(
    auth = KtorAuthConfig(environment)
)) {
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
    install(Authentication) {
        jwt("auth-jwt") {
            realm = config.auth.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.auth.secret))
                    .withAudience(config.auth.audience)
                    .withIssuer(config.auth.issuer)
                    .build()
            )
            validate { jwtCredential: JWTCredential ->
                when {
                    !jwtCredential.payload.audience.contains(config.auth.audience) -> {
                        log.error("Unsupported audience in JWT token ${jwtCredential.payload.audience}. Must be ${config.auth.audience}")
                        null
                    }
                    jwtCredential.payload.issuer != config.auth.issuer -> {
                        log.error("Wrong issuer in JWT token ${jwtCredential.payload.issuer}. Must be ${config.auth.issuer}")
                        null
                    }
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        null
                    }
                    else -> {
                        JWTPrincipal(jwtCredential.payload)
                    }
                }
            }
        }
    }

    val service = config.finTransactionService
    val objectMapper = config.objectMapper
    val wsUserSessions = config.userSessions

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authenticate("auth-jwt") {
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
        }
        webSocket("ws") { handleSession(objectMapper, service, wsUserSessions) }
    }
}