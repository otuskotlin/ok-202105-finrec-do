package com.finyou.fintrack.backend.common.models

data class TransactionModel(
    val id: TransactionIdModel = TransactionIdModel.NONE,
    val userId: UserIdModel = UserIdModel.NONE,
    val name: String = "",
    val description: String = "",
    val date: Long = Long.MIN_VALUE,
    val transactionType: TypeModel = TypeModel.NONE,
    val amount: Double = Double.MIN_VALUE,
    val currency: CurrencyModel = CurrencyModel.NONE
) {
    companion object {
        val NONE = TransactionModel()
    }
}
