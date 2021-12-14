package com.finyou.fintrack.backend.stubs

import com.finyou.fintrack.backend.common.models.*
import java.time.Instant
import kotlin.random.Random

object StubTransactions {
    const val id = "441184"
    val idModel = FinTransactionIdModel(id)
    val permissionsDefault = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE, PermissionModel.DELETE)
    const val userId = "123"
    val userIdModel = UserIdModel(id = userId)

    private val incomeTransaction = FinTransactionModel(
        id = idModel,
        userId = userIdModel,
        name = "Salary",
        description = "Monthly salary",
        date = Instant.now().minusSeconds(50000L),
        transactionType = TypeModel.INCOME,
        amountCurrency = AmountCurrencyModel(
            amount = 2000.toBigDecimal(),
            currency = "USD"
        ),
        permissions = permissionsDefault
    )
    private val outcomeTransaction = FinTransactionModel(
        id = idModel,
        userId = userIdModel,
        name = "TV",
        description = "TV Samsung 55\"",
        date = Instant.now().minusSeconds(5000L),
        transactionType = TypeModel.OUTCOME,
        amountCurrency = AmountCurrencyModel(
            amount = 1200.toBigDecimal(),
            currency = "USD"
        ),
        permissions = permissionsDefault
    )

    fun getStub(model: (FinTransactionModel.() -> Unit)? = null): FinTransactionModel {
        return incomeTransaction.also { stub ->
            model?.let { stub.apply(it) }
        }
    }

    fun getStubs() = mutableListOf(
        getStub { id = FinTransactionIdModel("45646") },
        getStub { id = FinTransactionIdModel("123") },
        getStub { id = FinTransactionIdModel("7899") }
    )

    fun FinTransactionModel.update(transaction: FinTransactionModel) = apply {
        name = transaction.name
        description = transaction.description
        date = transaction.date
        transactionType = transaction.transactionType
        amountCurrency = transaction.amountCurrency
        permissions.let {
            it.clear()
            it.addAll(transaction.permissions)
        }
    }
}