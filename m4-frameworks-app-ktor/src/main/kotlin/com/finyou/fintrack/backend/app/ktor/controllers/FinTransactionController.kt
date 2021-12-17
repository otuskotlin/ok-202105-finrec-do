package com.finyou.fintrack.backend.app.ktor.controllers

import com.finyou.fintrack.backend.app.ktor.mappers.toModel
import com.finyou.fintrack.backend.app.ktor.services.FinTransactionService
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.openapi.models.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.request.*
import io.ktor.response.*

suspend fun ApplicationCall.init(service: FinTransactionService) {
    val request = receive<InitTransactionRequest>()
    val context = FtContext(
        principal = principal<JWTPrincipal>().toModel()
    )
    val response = try {
        service.init(context, request)
    } catch (e: Throwable) {
        service.errorAd(context, e) as InitTransactionResponse
    }
    respond(response)
}

suspend fun ApplicationCall.createFinTransaction(service: FinTransactionService) {
    val request = receive<CreateTransactionRequest>()
    val context = FtContext(
        principal = principal<JWTPrincipal>().toModel()
    )
    val response = try {
        service.create(context, request)
    } catch (e: Throwable) {
        service.errorAd(context, e) as CreateTransactionResponse
    }
    respond(response)
}

suspend fun ApplicationCall.readFinTransaction(service: FinTransactionService) {
    val request = receive<ReadTransactionRequest>()
    val context = FtContext(
        principal = principal<JWTPrincipal>().toModel()
    )
    val response = try {
        service.read(context, request)
    } catch (e: Throwable) {
        service.errorAd(context, e) as CreateTransactionResponse
    }
    respond(response)
}

suspend fun ApplicationCall.updateFinTransaction(service: FinTransactionService) {
    val request = receive<UpdateTransactionRequest>()
    val context = FtContext(
        principal = principal<JWTPrincipal>().toModel()
    )
    val response = try {
        service.update(context, request)
    } catch (e: Throwable) {
        service.errorAd(context, e) as UpdateTransactionResponse
    }
    respond(response)
}

suspend fun ApplicationCall.deleteFinTransaction(service: FinTransactionService) {
    val request = receive<DeleteTransactionRequest>()
    val context = FtContext(
        principal = principal<JWTPrincipal>().toModel()
    )
    val response = try {
        service.delete(context, request)
    } catch (e: Throwable) {
        service.errorAd(context, e) as DeleteTransactionResponse
    }
    respond(response)
}

suspend fun ApplicationCall.searchFinTransaction(service: FinTransactionService) {
    val request = receive<SearchTransactionRequest>()
    val context = FtContext(
        principal = principal<JWTPrincipal>().toModel()
    )
    val response = try {
        service.search(context, request)
    } catch (e: Throwable) {
        service.errorAd(context, e) as SearchTransactionResponse
    }
    respond(response)
}