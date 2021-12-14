package com.finyou.fintrack.backend.repo.test

import com.finyou.fintrack.backend.common.models.*
import java.time.Instant
import java.util.*

abstract class BaseInitFinTransactions: IInitObjects<FinTransactionModel> {

    fun createInitTestModel(
        suf: String,
        userId: UserIdModel = UserIdModel(UUID.randomUUID().toString()),
        transactionType: TypeModel = TypeModel.INCOME,
    ) = FinTransactionModel(
        id = FinTransactionIdModel(UUID.randomUUID().toString()),
        userId = userId,
        name = "$suf stub",
        description = "$suf stub description",
        date = Instant.ofEpochMilli(Instant.now().minusSeconds(50000L).toEpochMilli()),
        transactionType = transactionType,
        amountCurrency = AmountCurrencyModel(
            amount = 2000.0.toBigDecimal(),
            currency = "USD"
        ),
        permissions = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE)
    )
}