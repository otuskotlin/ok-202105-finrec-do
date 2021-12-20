package com.finyou.fintrack.backend.transport.openapi

import com.finyou.fintrack.openapi.models.ResultTransaction

data class FtLogModel(
    val requestFinTransactionId: String? = null,
    val requestFinTransaction: ResultTransaction? = null,
    val responseFinTransaction: ResultTransaction? = null,
    val responseFinTransactions: List<ResultTransaction>? = null
)
