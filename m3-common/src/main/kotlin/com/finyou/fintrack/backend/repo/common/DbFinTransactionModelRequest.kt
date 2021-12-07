package com.finyou.fintrack.backend.repo.common

import com.finyou.fintrack.backend.common.models.FinTransactionModel

data class DbFinTransactionModelRequest(
    val finTransaction: FinTransactionModel
) : IDbRequest
