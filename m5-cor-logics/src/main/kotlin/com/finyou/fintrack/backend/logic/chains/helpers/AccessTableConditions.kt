package com.finyou.fintrack.backend.logic.chains.helpers

import com.finyou.fintrack.backend.common.models.FinPrincipalRelations
import com.finyou.fintrack.backend.common.models.FinTransactionOperation
import com.finyou.fintrack.backend.common.models.FinUserPermissions

data class AccessTableConditions(
    val operation: FinTransactionOperation,
    val permission: FinUserPermissions,
    val relation: FinPrincipalRelations
)

val accessTable = mapOf(
    // Create
    AccessTableConditions(
        operation = FinTransactionOperation.CREATE,
        permission = FinUserPermissions.CREATE_OWN,
        relation = FinPrincipalRelations.NONE
    ) to true,

    // Read
    AccessTableConditions(
        operation = FinTransactionOperation.READ,
        permission = FinUserPermissions.READ_OWN,
        relation = FinPrincipalRelations.OWN
    ) to true,

    // Update
    AccessTableConditions(
        operation = FinTransactionOperation.UPDATE,
        permission = FinUserPermissions.UPDATE_OWN,
        relation = FinPrincipalRelations.OWN
    ) to true,

    // Delete
    AccessTableConditions(
        operation = FinTransactionOperation.DELETE,
        permission = FinUserPermissions.DELETE_OWN,
        relation = FinPrincipalRelations.OWN
    ) to true,

    // Search
    AccessTableConditions(
        operation = FinTransactionOperation.SEARCH,
        permission = FinUserPermissions.SEARCH_OWN,
        relation = FinPrincipalRelations.OWN
    ) to true,
)