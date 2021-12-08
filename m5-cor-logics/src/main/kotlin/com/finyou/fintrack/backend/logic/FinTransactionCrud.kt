package com.finyou.fintrack.backend.logic

import com.finyou.fintrack.backend.common.context.ContextConfig
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.logic.chains.FinCreate
import com.finyou.fintrack.backend.logic.chains.FinDelete
import com.finyou.fintrack.backend.logic.chains.FinRead
import com.finyou.fintrack.backend.logic.chains.FinSearch
import com.finyou.fintrack.backend.logic.chains.FinUpdate

class FinTransactionCrud(val config: ContextConfig = ContextConfig()) {
    suspend fun create(context: FtContext) {
        FinCreate.exec(context.initSettings())
    }
    suspend fun read(context: FtContext) {
        FinRead.exec(context.initSettings())
    }
    suspend fun update(context: FtContext) {
        FinUpdate.exec(context.initSettings())
    }
    suspend fun delete(context: FtContext) {
        FinDelete.exec(context.initSettings())
    }
    suspend fun search(context: FtContext) {
        FinSearch.exec(context.initSettings())
    }

    private fun FtContext.initSettings() = apply {
        config = this@FinTransactionCrud.config
    }
}