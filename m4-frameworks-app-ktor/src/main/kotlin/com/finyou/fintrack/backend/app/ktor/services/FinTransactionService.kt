package com.finyou.fintrack.backend.app.ktor.services

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.logging.ftLogger
import com.finyou.fintrack.backend.logic.FinTransactionCrud
import com.finyou.fintrack.backend.mapping.openapi.*
import com.finyou.fintrack.openapi.models.*
import org.slf4j.event.Level

class FinTransactionService(
    private val crud: FinTransactionCrud
) {

    private val logger = ftLogger(FinTransactionService::class.java)

    suspend fun init(context: FtContext, request: InitTransactionRequest): InitTransactionResponse {
        return context.handle("init-transaction", FtContext::toInitResponse) {
            context.setQuery(request)
        }
    }

    suspend fun create(context: FtContext, request: CreateTransactionRequest): CreateTransactionResponse {
        return context.handle("create-transaction", FtContext::toCreateResponse) {
            crud.create(it.setQuery(request))
        }
    }

    suspend fun read(context: FtContext, request: ReadTransactionRequest): ReadTransactionResponse {
        return context.handle("read-transaction", FtContext::toReadResponse) {
            crud.read(it.setQuery(request))
        }
    }

    suspend fun update(context: FtContext, request: UpdateTransactionRequest): UpdateTransactionResponse {
        return context.handle("update-transaction", FtContext::toUpdateResponse) {
            crud.update(it.setQuery(request))
        }
    }

    suspend fun delete(context: FtContext, request: DeleteTransactionRequest): DeleteTransactionResponse {
        return context.handle("delete-transaction", FtContext::toDeleteResponse) {
            crud.delete(it.setQuery(request))
        }
    }

    suspend fun search(context: FtContext, request: SearchTransactionRequest): SearchTransactionResponse {
        return context.handle("search-transaction", FtContext::toSearchResponse) {
            crud.search(it.setQuery(request))
        }
    }

    suspend fun errorAd(context: FtContext, e: Throwable): BaseMessage {
        return context.handle("errorAd-transaction", FtContext::toInitResponse) {
            context.addError(e)
        }
    }

    fun initWs(context: FtContext): BaseMessage {
        // TODO add logic to init ws
        return context.toInitResponse()
    }

    fun wsDisconnected(context: FtContext) {
        // TODO add logic to disconnect ws
    }

    private suspend fun <T> FtContext.handle(logId: String, mapper: FtContext.()->T, block: suspend (FtContext) -> Unit) : T {
        logger.log(
            msg = "Request got, query = {}",
            level = Level.INFO,
            data = toLog(logId)
        )
        block(this)
        return mapper().also {
            logger.log(
                msg = "Response ready got, response = {}",
                level = Level.INFO,
                data = toLog(logId)
            )
        }
    }
}