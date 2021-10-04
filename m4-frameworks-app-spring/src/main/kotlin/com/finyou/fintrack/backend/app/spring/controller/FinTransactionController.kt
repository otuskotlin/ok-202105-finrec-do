package com.finyou.fintrack.backend.app.spring.controller

import com.finyou.fintrack.backend.app.spring.service.FinTransactionService
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.mapping.openapi.*
import com.finyou.fintrack.openapi.models.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transaction")
class FinTransactionController(
    private val service: FinTransactionService
) {

    @PostMapping("init")
    fun init(@RequestBody initTransactionRequest: InitTransactionRequest) =
        FtContext().setQuery(initTransactionRequest).let {
            service.init(it)
        }.toInitResponse()

    @PostMapping("create")
    fun createFinTransaction(@RequestBody createTransactionRequest: CreateTransactionRequest) =
        FtContext().setQuery(createTransactionRequest).let {
            service.create(it)
        }.toCreateResponse()

    @PostMapping("read")
    fun readFinTransaction(@RequestBody readTransactionRequest: ReadTransactionRequest) =
        FtContext().setQuery(readTransactionRequest).let {
            service.read(it)
        }.toReadResponse()

    @PostMapping("update")
    fun updateFinTransaction(@RequestBody updateTransactionRequest: UpdateTransactionRequest) =
        FtContext().setQuery(updateTransactionRequest).let {
            service.update(it)
        }.toUpdateResponse()

    @PostMapping("delete")
    fun deleteFinTransaction(@RequestBody deleteTransactionRequest: DeleteTransactionRequest) =
        FtContext().setQuery(deleteTransactionRequest).let {
            service.delete(it)
        }.toDeleteResponse()

    @PostMapping("search")
    fun searchFinTransaction(@RequestBody searchTransactionRequest: SearchTransactionRequest) =
        FtContext().setQuery(searchTransactionRequest).let {
            service.search(it)
        }.toSearchResponse()
}