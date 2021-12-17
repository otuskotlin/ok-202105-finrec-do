package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinUserGroup
import com.finyou.fintrack.backend.common.models.FinUserPermissions
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker

fun ICorChainDsl<FtContext>.chainPermissions(title: String) = worker<FtContext> {
    this.title = title
    description = "Вычисление прав доступа для групп пользователей"

    on {
        status == CorStatus.RUNNING
    }

    handle {
        val permAdd: Set<FinUserPermissions> = principal.groups.map {
            when(it) {
                FinUserGroup.USER -> setOf(
                    FinUserPermissions.READ_OWN,
                    FinUserPermissions.READ_PUBLIC,
                    FinUserPermissions.CREATE_OWN,
                    FinUserPermissions.UPDATE_OWN,
                    FinUserPermissions.DELETE_OWN,
                    FinUserPermissions.OFFER_FOR_OWN,
                )
                FinUserGroup.MODERATOR_MP -> setOf()
                FinUserGroup.ADMIN_AD -> setOf()
                FinUserGroup.TEST -> setOf()
                FinUserGroup.BAN_AD -> setOf()
            }
        }.flatten().toSet()
        val permDel: Set<FinUserPermissions> = principal.groups.map {
            when(it) {
                FinUserGroup.USER -> setOf()
                FinUserGroup.MODERATOR_MP -> setOf()
                FinUserGroup.ADMIN_AD -> setOf()
                FinUserGroup.TEST -> setOf()
                FinUserGroup.BAN_AD -> setOf(
                    FinUserPermissions.UPDATE_OWN,
                    FinUserPermissions.CREATE_OWN,
                )
            }
        }.flatten().toSet()
        chainPermissions.addAll(permAdd)
        chainPermissions.removeAll(permDel)
    }
}