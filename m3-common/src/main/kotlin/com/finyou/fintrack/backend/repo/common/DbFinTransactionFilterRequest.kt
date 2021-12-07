package com.finyou.fintrack.backend.repo.common

import com.finyou.fintrack.backend.common.models.FilterModel
import com.finyou.fintrack.backend.common.models.UserIdModel

data class DbFinTransactionFilterRequest(
    val searchStr: String = "",
    val userId: UserIdModel = UserIdModel.NONE,
    val filter: FilterModel = FilterModel.NONE
) : IDbRequest
