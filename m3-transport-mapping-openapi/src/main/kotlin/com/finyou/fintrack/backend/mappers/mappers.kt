package com.finyou.fintrack.backend.mappers

import com.finyou.fintrack.openapi.models.CreatableTransaction
import com.finyou.fintrack.openapi.models.CreateTransactionRequest
import com.finyou.fintrack.openapi.models.TransactionType
import com.finyou.fintrack.backend.context.FtContext
import com.finyou.fintrack.backend.models.CurrencyModel
import com.finyou.fintrack.backend.models.TransactionModel
import com.finyou.fintrack.backend.models.TypeModel
import com.finyou.fintrack.backend.models.UserIdModel
import java.util.*

fun FtContext.setQuery(query: CreateTransactionRequest) {
    requestTransaction = query.createTransaction?.inner ?: TransactionModel.NONE
}

private val CreatableTransaction.inner: TransactionModel
    get() {
        return TransactionModel(
            userId = userId?.let { UserIdModel(it) } ?: UserIdModel.NONE,
            name = name ?: "",
            description = description ?: "",
            date = convertCreateDate(date, time),
            transactionType = transactionType?.inner ?: TypeModel.NONE,
            amount = amount ?: Double.MIN_VALUE,
            currency = currency?.let { CurrencyModel(it) } ?: CurrencyModel.NONE
        )
    }

private val TransactionType.inner: TypeModel
    get() = when(this) {
        TransactionType.INCOME -> TypeModel.INCOME
        TransactionType.OUTCOME -> TypeModel.OUTCOME
    }

private fun convertCreateDate(date: String?, time: String?): Long {
    val transactionDate = Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    date?.let { safeDate ->
        val dateParts = safeDate.split(".")
        if (dateParts.size == 3) {
            dateParts[0].toIntOrNull()?.let {
                transactionDate.set(Calendar.DAY_OF_MONTH, it)
            }
            dateParts[1].toIntOrNull()?.let {
                transactionDate.set(Calendar.MONTH, it)
            }
            dateParts[2].toIntOrNull()?.let {
                transactionDate.set(Calendar.YEAR, it)
            }
        }
    }
    time?.let { safeTime ->
        val timeParts = safeTime.split(":")
        if (timeParts.size == 2) {
            timeParts[0].toIntOrNull()?.let {
                transactionDate.set(Calendar.HOUR_OF_DAY, it)
            }
            timeParts[1].toIntOrNull()?.let {
                transactionDate.set(Calendar.MINUTE, it)
            }
        }
    }
    return transactionDate.timeInMillis
}