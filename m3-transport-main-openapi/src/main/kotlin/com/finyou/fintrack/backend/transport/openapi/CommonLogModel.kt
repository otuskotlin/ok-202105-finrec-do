package com.finyou.fintrack.backend.transport.openapi

import com.finyou.fintrack.openapi.models.ApiError

data class CommonLogModel(
    val messageId: String? = null,
    val messageTime: String? = null,
    val logId: String? = null,
    val source: String? = null,
    val ftLog: FtLogModel? = null,
    val errors: List<ApiError>? = null,
)
