package com.finyou.fintrack.backend.repo.common

import com.finyou.fintrack.backend.common.models.ErrorModel
import com.finyou.fintrack.backend.common.models.FinTransactionModel

data class DbFinTransactionResponse(
    override val isSuccess: Boolean,
    override val errors: List<ErrorModel> = emptyList(),
    override val result: FinTransactionModel?
) : IDbResponse<FinTransactionModel?>
