package com.finyou.fintrack.backend.logic

import com.finyou.fintrack.backend.common.models.FinPrincipalModel
import com.finyou.fintrack.backend.common.models.FinUserGroup
import com.finyou.fintrack.backend.common.models.UserIdModel
import com.finyou.fintrack.backend.stubs.StubTransactions

fun principalUser(id: UserIdModel = StubTransactions.userIdModel, banned: Boolean = false) = FinPrincipalModel(
    id = id,
    groups = setOf(
        FinUserGroup.USER,
        FinUserGroup.TEST,
        if (banned) FinUserGroup.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)

fun principalModer(id: UserIdModel = StubTransactions.userIdModel, banned: Boolean = false) = FinPrincipalModel(
    id = id,
    groups = setOf(
        FinUserGroup.MODERATOR_MP,
        FinUserGroup.TEST,
        if (banned) FinUserGroup.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)

fun principalAdmin(id: UserIdModel = StubTransactions.userIdModel, banned: Boolean = false) = FinPrincipalModel(
    id = id,
    groups = setOf(
        FinUserGroup.ADMIN_AD,
        FinUserGroup.TEST,
        if (banned) FinUserGroup.BAN_AD else null
    )
        .filterNotNull()
        .toSet()
)