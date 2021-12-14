package com.finyou.fintrack.backend.repo.common

import com.finyou.fintrack.backend.common.models.ErrorModel
import com.finyou.fintrack.backend.common.models.FinTransactionModel

data class DbFinTransactionsResponse(
    override val isSuccess: Boolean,
    override val errors: List<ErrorModel> = emptyList(),
    override val result: List<FinTransactionModel>
) : IDbResponse<List<FinTransactionModel>>
