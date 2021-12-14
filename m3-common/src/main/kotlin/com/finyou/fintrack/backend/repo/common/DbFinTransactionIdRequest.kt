package com.finyou.fintrack.backend.repo.common

import com.finyou.fintrack.backend.common.models.FinTransactionIdModel

data class DbFinTransactionIdRequest(
    val id: FinTransactionIdModel
) : IDbRequest
