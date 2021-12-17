package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.ErrorModel
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.cor.common.handlers.worker
import com.finyou.fintrack.backend.logic.chains.helpers.AccessTableConditions
import com.finyou.fintrack.backend.logic.chains.helpers.accessTable
import com.finyou.fintrack.backend.logic.chains.helpers.resolveRelationsTo

fun ICorChainDsl<FtContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { status == CorStatus.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        dbTransaction.principalRelations = dbTransaction.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = dbTransaction.principalRelations.flatMap { relation ->
            chainPermissions.map { permission ->
                AccessTableConditions(
                    operation = operation,
                    permission = permission,
                    relation = relation,
                )
            }
        }
            .any {
                accessTable[it] ?: false
            }
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            addError(
                ErrorModel(message = "User is not allowed to this operation")
            )
        }
    }
}