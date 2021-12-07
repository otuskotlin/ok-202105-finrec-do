package com.finyou.fintrack.backend.app.ktor.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finyou.fintrack.backend.app.ktor.models.KtorUserSession
import com.finyou.fintrack.backend.app.ktor.services.FinTransactionService
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.openapi.models.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException

suspend fun WebSocketSession.handleSession(
    objectMapper: ObjectMapper,
    service: FinTransactionService,
    userSessions: MutableSet<KtorUserSession>
) {

    val userSession = KtorUserSession(this, objectMapper)
    userSessions.add(userSession)

    val initContext = FtContext(userSession = userSession)
    val initResponse = try {
        service.initWs(initContext)
    } catch (e: Throwable) {
        service.errorAd(initContext, e)
    }
    outgoing.send(Frame.Text(objectMapper.writeValueAsString(initResponse)))

    try {
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val context = FtContext(userSession = userSession)
                val response = try {
                    val request = objectMapper.readValue<BaseMessage>(frame.readText())
                    when (request) {
                        is InitTransactionRequest -> service.init(context, request)
                        is CreateTransactionRequest -> service.create(context, request)
                        is UpdateTransactionRequest -> service.update(context, request)
                        is ReadTransactionRequest -> service.read(context, request)
                        is DeleteTransactionRequest -> service.delete(context, request)
                        is SearchTransactionRequest -> service.search(context, request)
                        else -> throw UnsupportedOperationException("Unknown request type")
                    }
                } catch (e: Throwable) {
                    service.errorAd(context, e)
                }
                outgoing.send(Frame.Text(objectMapper.writeValueAsString(response)))
            }
        }

    } catch (e: ClosedReceiveChannelException) {
        println("Session closed by user")
    } finally {
        userSessions.remove(userSession)
    }

    val context = FtContext(userSession = userSession)
    try {
        service.wsDisconnected(context)
    } catch (e: Throwable) {
        service.errorAd(context, e)
    }
}