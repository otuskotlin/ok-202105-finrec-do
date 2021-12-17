package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.FinUserGroup
import com.finyou.fintrack.backend.common.models.PermissionModel
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.cor.common.handlers.worker

fun ICorChainDsl<FtContext>.frontPermissions(title: String) = chain {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { status == CorStatus.RUNNING }

    worker {
        this.title = "Разрешения для собственного объявления"
        description = this.title
        on { responseTransaction.userId == principal.id }
        handle {
            val permAdd: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    FinUserGroup.USER -> setOf(
                        PermissionModel.READ,
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                    )
                    FinUserGroup.MODERATOR_MP -> setOf()
                    FinUserGroup.ADMIN_AD -> setOf()
                    FinUserGroup.TEST -> setOf()
                    FinUserGroup.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    FinUserGroup.USER -> setOf()
                    FinUserGroup.MODERATOR_MP -> setOf()
                    FinUserGroup.ADMIN_AD -> setOf()
                    FinUserGroup.TEST -> setOf()
                    FinUserGroup.BAN_AD -> setOf(
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                    )
                }
            }.flatten().toSet()
            responseTransaction.permissions.addAll(permAdd)
            responseTransaction.permissions.removeAll(permDel)
        }
    }

    worker {
        this.title = "Разрешения для модератора"
        description = this.title
        on { responseTransaction.userId != principal.id /* && tag, group, ... */ }
        handle {
            val permAdd: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    FinUserGroup.USER -> setOf()
                    FinUserGroup.MODERATOR_MP -> setOf(
                        PermissionModel.READ,
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                    )
                    FinUserGroup.ADMIN_AD -> setOf()
                    FinUserGroup.TEST -> setOf()
                    FinUserGroup.BAN_AD -> setOf()
                }
            }.flatten().toSet()
            val permDel: Set<PermissionModel> = principal.groups.map {
                when (it) {
                    FinUserGroup.USER -> setOf(
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                    )
                    FinUserGroup.MODERATOR_MP -> setOf()
                    FinUserGroup.ADMIN_AD -> setOf()
                    FinUserGroup.TEST -> setOf()
                    FinUserGroup.BAN_AD -> setOf(
                        PermissionModel.UPDATE,
                        PermissionModel.DELETE,
                    )
                }
            }.flatten().toSet()
            responseTransaction.permissions.addAll(permAdd)
            responseTransaction.permissions.removeAll(permDel)
        }
    }
    worker {
        this.title = "Разрешения для администратора"
        description = this.title
    }
}