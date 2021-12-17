package com.finyou.fintrack.backend.logic.workers

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.AppModeModel
import com.finyou.fintrack.backend.common.models.FinUserGroup
import com.finyou.fintrack.backend.common.models.StubCaseModel
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction

internal fun ICorChainDsl<FtContext>.chooseDb(title: String) = worker {
    this.title = title
    description = """
        Here we choose either prod or test DB repository. 
        In case of STUB request here we use empty repo and set stubCase=SUCCESS if unset
    """.trimIndent()

    handle {
        if (principal.groups.contains(FinUserGroup.TEST)) {
            finTransactionRepo = config.repoTest
            return@handle
        }
        finTransactionRepo = when(appMode) {
            AppModeModel.PROD -> config.repoProd
            AppModeModel.TEST -> config.repoTest
            AppModeModel.STUB -> {
                if (stubCase == StubCaseModel.NONE){
                    stubCase = StubCaseModel.SUCCESS
                }
                IRepoFinTransaction.NONE
            }
        }
    }
}