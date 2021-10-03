package com.finyou.fintrack.backend.app.serverless.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import com.finyou.fintrack.backend.app.serverless.services.FinTransactionService
import com.finyou.fintrack.backend.app.serverless.toAPIGatewayV2HTTPResponse
import com.finyou.fintrack.backend.app.serverless.toTransportModel
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.mapping.openapi.*
import com.finyou.fintrack.openapi.models.*

val service = FinTransactionService()

class CreateTransactionHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse {
        return FtContext()
            .setQuery(input.toTransportModel<CreateTransactionRequest>())
            .let { service.create(it) }
            .toCreateResponse()
            .toAPIGatewayV2HTTPResponse()
    }
}

class ReadTransactionHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse {
        return FtContext()
            .setQuery(input.toTransportModel<ReadTransactionRequest>())
            .let { service.read(it) }
            .toReadResponse()
            .toAPIGatewayV2HTTPResponse()
    }
}

class UpdateTransactionHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse {
        return FtContext()
            .setQuery(input.toTransportModel<UpdateTransactionRequest>())
            .let { service.update(it) }
            .toUpdateResponse()
            .toAPIGatewayV2HTTPResponse()
    }
}

class DeleteTransactionHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse {
        return FtContext()
            .setQuery(input.toTransportModel<DeleteTransactionRequest>())
            .let { service.delete(it) }
            .toDeleteResponse()
            .toAPIGatewayV2HTTPResponse()
    }
}

class SearchTransactionHandler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    override fun handleRequest(input: APIGatewayV2HTTPEvent, context: Context): APIGatewayV2HTTPResponse {
        return FtContext()
            .setQuery(input.toTransportModel<SearchTransactionRequest>())
            .let { service.search(it) }
            .toSearchResponse()
            .toAPIGatewayV2HTTPResponse()
    }
}