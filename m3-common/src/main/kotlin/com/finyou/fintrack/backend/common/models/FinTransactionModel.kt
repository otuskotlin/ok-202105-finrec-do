package com.finyou.fintrack.backend.common.models

import java.time.Instant

data class FinTransactionModel(
    var id: FinTransactionIdModel = FinTransactionIdModel.NONE,
    var userId: UserIdModel = UserIdModel.NONE,
    var name: String = "",
    var description: String = "",
    var date: Instant = Instant.MIN,
    var transactionType: TypeModel = TypeModel.NONE,
    var amountCurrency: AmountCurrencyModel = AmountCurrencyModel.NONE,
    var permissions: MutableSet<PermissionModel> = mutableSetOf()
) {
    companion object {
        val NONE = FinTransactionModel()
    }
}
