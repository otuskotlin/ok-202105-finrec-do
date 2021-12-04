package com.finyou.fintrack.backend.app.ktor.services

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.logic.FinTransactionCrud
import com.finyou.fintrack.backend.mapping.openapi.*
import com.finyou.fintrack.openapi.models.*

class FinTransactionService(
    private val crud: FinTransactionCrud
) {

    fun init(context: FtContext, request: InitTransactionRequest): InitTransactionResponse {
        context.setQuery(request)
        return context.toInitResponse()
    }

    suspend fun create(context: FtContext, request: CreateTransactionRequest): CreateTransactionResponse {
        crud.create(context.setQuery(request))
        return context.toCreateResponse()
    }

    suspend fun read(context: FtContext, request: ReadTransactionRequest): ReadTransactionResponse {
        crud.read(context.setQuery(request))
        return context.toReadResponse()
    }

    suspend fun update(context: FtContext, request: UpdateTransactionRequest): UpdateTransactionResponse {
        crud.update(context.setQuery(request))
        return context.toUpdateResponse()
    }

    suspend fun delete(context: FtContext, request: DeleteTransactionRequest): DeleteTransactionResponse {
        crud.delete(context.setQuery(request))
        return context.toDeleteResponse()
    }

    suspend fun search(context: FtContext, request: SearchTransactionRequest): SearchTransactionResponse {
        crud.search(context.setQuery(request))
        return context.toSearchResponse()
    }

    fun errorAd(context: FtContext, e: Throwable): BaseMessage {
        context.addError(e)
        return context.toInitResponse()
    }

    fun initWs(context: FtContext): BaseMessage {
        // TODO add logic to init ws
        return context.toInitResponse()
    }

    fun wsDisconnected(context: FtContext) {
        // TODO add logic to disconnect ws
    }
}