package com.finyou.fintrack.backend.stubs

import com.finyou.fintrack.backend.common.models.*
import java.time.Instant
import kotlin.random.Random

object StubTransactions {
    const val id = "441184"
    const val userId = "123"

    private val incomeTransaction = FinTransactionModel(
        id = FinTransactionIdModel(id = id),
        userId = UserIdModel(id = userId),
        name = "Salary",
        description = "Monthly salary",
        date = Instant.now().minusSeconds(50000L),
        transactionType = TypeModel.INCOME,
        amountCurrency = AmountCurrencyModel(
            amount = 2000.toBigDecimal(),
            currency = "USD"
        ),
        permissions = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE)
    )
    private val outcomeTransaction = FinTransactionModel(
        id = FinTransactionIdModel(id = id),
        userId = UserIdModel(id = userId),
        name = "TV",
        description = "TV Samsung 55\"",
        date = Instant.now().minusSeconds(5000L),
        transactionType = TypeModel.OUTCOME,
        amountCurrency = AmountCurrencyModel(
            amount = 1200.toBigDecimal(),
            currency = "USD"
        ),
        permissions = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE, PermissionModel.DELETE)
    )

    private val randomTransaction: FinTransactionModel
        get() = if (Random.nextBoolean()) incomeTransaction else outcomeTransaction

    fun getStub(model: (FinTransactionModel.() -> Unit)? = null) = randomTransaction.also { stub ->
        model?.let { stub.apply(it) }
    }

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