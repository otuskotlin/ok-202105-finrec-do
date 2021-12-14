package com.finyou.fintrack.backend.repo.inmemory.models

import com.finyou.fintrack.backend.common.models.*
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant

data class FinTransactionRow(
    val id: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val date: Instant? = null,
    val transactionType: String? = null,
    val amount: BigDecimal? = null,
    val currency: String? = null,
    val permissions: List<String>? = null
) : Serializable {
    constructor(internal: FinTransactionModel) : this(
        id = internal.id.toString().takeIf { it.isNotBlank() },
        userId = internal.userId.toString().takeIf { it.isNotBlank() },
        name = internal.name.takeIf { it.isNotBlank() },
        description = internal.description.takeIf { it.isNotBlank() },
        date = internal.date.takeIf { it > Instant.MIN },
        transactionType = internal.transactionType.takeIf { it != TypeModel.NONE }?.name,
        amount = internal.amountCurrency.amount.takeIf { it > BigDecimal.ZERO },
        currency = internal.amountCurrency.currency.takeIf { it.isNotBlank() },
        permissions = internal.permissions.takeIf { it.isNotEmpty() }?.map { it.name }?.toList()
    )

    fun toInternal(): FinTransactionModel = FinTransactionModel(
        id = id?.let { FinTransactionIdModel(it) } ?: FinTransactionIdModel.NONE,
        userId = userId?.let { UserIdModel(it) } ?: UserIdModel.NONE,
        name = name ?: "",
        description = description ?: "",
        date = date ?: Instant.MIN,
        transactionType = transactionType?.let { TypeModel.valueOf(it) } ?: TypeModel.NONE,
        amountCurrency = if (amount == null || currency == null)
            AmountCurrencyModel.NONE
        else AmountCurrencyModel(amount, currency),
        permissions = permissions?.map { PermissionModel.valueOf(it) }?.toMutableSet() ?: mutableSetOf()
    )
}
