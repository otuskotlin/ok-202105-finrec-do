package com.finyou.fintrack.backend.app.ktor.models

import com.fasterxml.jackson.databind.ObjectMapper
import com.finyou.fintrack.backend.common.models.IUserSession
import com.finyou.fintrack.openapi.models.BaseMessage
import io.ktor.http.cio.websocket.*

class KtorUserSession(
    override val fwSession: WebSocketSession,
    private val objectMapper: ObjectMapper
) : IUserSession<WebSocketSession, BaseMessage> {
    override suspend fun send(message: BaseMessage) {
        fwSession.send(Frame.Text(objectMapper.writeValueAsString(message)))
    }
}
