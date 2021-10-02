package com.finyou.fintrack.backend.app.ktor.services

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinTransactionIdModel
import com.finyou.fintrack.backend.common.models.PaginatedResponseModel
import com.finyou.fintrack.backend.stubs.StubTransactions

class FinTransactionService {

    fun init(context: FtContext): FtContext {
        return context
    }

    fun create(context: FtContext): FtContext {
        val createdTransaction = context.requestTransaction.apply {
            id = FinTransactionIdModel(StubTransactions.id)
        }
        return context.apply {
            responseTransaction = createdTransaction
        }
    }

    fun read(context: FtContext): FtContext {
        val readTransaction = StubTransactions.getStub {
            id = context.requestTransactionId
        }
        return context.apply {
            responseTransaction = readTransaction
        }
    }

    fun update(context: FtContext): FtContext {
        val updatedTransaction = context.requestTransaction
        return context.apply {
            responseTransaction = updatedTransaction
        }
    }

    fun delete(context: FtContext): FtContext {
        val deletedTransaction = StubTransactions.getStub {
            id = FinTransactionIdModel(StubTransactions.id)
        }
        return context.apply {
            responseTransaction = deletedTransaction
        }
    }

    fun search(context: FtContext): FtContext {
        val items = mutableListOf(
            StubTransactions.getStub(),
            StubTransactions.getStub(),
            StubTransactions.getStub(),
        )
        val responsePagination = PaginatedResponseModel(
            lastId = FinTransactionIdModel(id = StubTransactions.id),
            total = 70,
            left = 50
        )
        return context.apply {
            responseTransactions = items
            responsePage = responsePagination
        }
    }
}