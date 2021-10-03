package com.finyou.fintrack.backend.app.serverless

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.finyou.fintrack.openapi.models.BaseMessage
import java.util.*

val mapper = jacksonObjectMapper().findAndRegisterModules()

inline fun <reified T> APIGatewayV2HTTPEvent.toTransportModel(): T =
    if (isBase64Encoded)
        mapper.readValue(Base64.getDecoder().decode(body))
    else mapper.readValue(body)

fun BaseMessage.toAPIGatewayV2HTTPResponse() : APIGatewayV2HTTPResponse =
    APIGatewayV2HTTPResponse.builder()
        .withStatusCode(200)
        .withHeaders(mapOf("Content-Type" to "application/json"))
        .withBody(mapper.writeValueAsString(this))
        .build()