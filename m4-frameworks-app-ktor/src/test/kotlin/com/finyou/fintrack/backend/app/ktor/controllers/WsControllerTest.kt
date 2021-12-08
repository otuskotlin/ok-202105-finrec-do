package com.finyou.fintrack.backend.app.ktor.controllers

import com.finyou.fintrack.backend.app.ktor.mapper
import com.finyou.fintrack.backend.app.ktor.module
import com.fasterxml.jackson.module.kotlin.readValue
import com.finyou.fintrack.openapi.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.cio.websocket.*
import io.ktor.server.testing.*

class WsControllerTest : FunSpec({
    test("webSocket") {
        withTestApplication({ module() }) {
            handleWebSocketConversation("/ws") { incoming, outgoing ->
                run {
                    val responseFrame = incoming.receive()
                    val response = mapper.readValue<BaseMessage>(responseFrame.readBytes())
                    response.shouldBeInstanceOf<InitTransactionResponse>()
                }

                val request = CreateTransactionRequest(
                    createTransaction = creatableTransaction,
                    debug = stub
                )
                val requestFrame = Frame.Text(mapper.writeValueAsString(request))
                outgoing.send(requestFrame)

                val responseFrame = incoming.receive()
                val response = mapper.readValue<BaseMessage>(responseFrame.readBytes())
                response.shouldBeInstanceOf<CreateTransactionResponse>()
            }
        }
    }
})