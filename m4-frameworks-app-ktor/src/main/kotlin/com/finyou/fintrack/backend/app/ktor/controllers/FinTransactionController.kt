package com.finyou.fintrack.backend.app.ktor.controllers

import com.finyou.fintrack.backend.app.ktor.services.FinTransactionService
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.mapping.openapi.*
import com.finyou.fintrack.openapi.models.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*

suspend fun ApplicationCall.init(service: FinTransactionService) {
    val initTransactionRequest = receive<InitTransactionRequest>()
    respond(FtContext().setQuery(initTransactionRequest).let {
        service.init(it)
    }.toInitResponse())
}

suspend fun ApplicationCall.createFinTransaction(service: FinTransactionService) {
    val createTransactionRequest = receive<CreateTransactionRequest>()
    respond(FtContext().setQuery(createTransactionRequest).let {
        service.create(it)
    }.toCreateResponse())
}

suspend fun ApplicationCall.readFinTransaction(service: FinTransactionService) {
    val readTransactionRequest = receive<ReadTransactionRequest>()
    respond(FtContext().setQuery(readTransactionRequest).let {
        service.read(it)
    }.toReadResponse())
}

suspend fun ApplicationCall.updateFinTransaction(service: FinTransactionService) {
    val updateTransactionRequest = receive<UpdateTransactionRequest>()
    respond(FtContext().setQuery(updateTransactionRequest).let {
        service.update(it)
    }.toUpdateResponse())
}

suspend fun ApplicationCall.deleteFinTransaction(service: FinTransactionService) {
    val deleteTransactionRequest = receive<DeleteTransactionRequest>()
    respond(FtContext().setQuery(deleteTransactionRequest).let {
        service.delete(it)
    }.toDeleteResponse())
}

suspend fun ApplicationCall.searchFinTransaction(service: FinTransactionService) {
    val searchTransactionRequest = receive<SearchTransactionRequest>()
    respond(FtContext().setQuery(searchTransactionRequest).let {
        service.search(it)
    }.toSearchResponse())
}