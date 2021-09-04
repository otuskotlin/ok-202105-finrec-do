package com.finyou.fintrack.backend.common.models

import java.time.Instant

data class FinTransactionModel(
    val id: FinTransactionIdModel = FinTransactionIdModel.NONE,
    val userId: UserIdModel = UserIdModel.NONE,
    val name: String = "",
    val description: String = "",
    val date: Instant = Instant.MIN,
    val transactionType: TypeModel = TypeModel.NONE,
    val amountCurrency: AmountCurrencyModel = AmountCurrencyModel.NONE,
    val permissions: MutableSet<PermissionModel> = mutableSetOf()
) {
    companion object {
        val NONE = FinTransactionModel()
    }
}
