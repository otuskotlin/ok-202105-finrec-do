package com.finyou.fintrack.backend.logic.chains.helpers

import com.finyou.fintrack.backend.common.models.FinPrincipalModel
import com.finyou.fintrack.backend.common.models.FinPrincipalRelations
import com.finyou.fintrack.backend.common.models.FinTransactionModel

fun FinTransactionModel.resolveRelationsTo(principal: FinPrincipalModel): Set<FinPrincipalRelations> = listOf(
    FinPrincipalRelations.NONE,
    FinPrincipalRelations.OWN.takeIf { principal.id == userId },
).filterNotNull().toSet()