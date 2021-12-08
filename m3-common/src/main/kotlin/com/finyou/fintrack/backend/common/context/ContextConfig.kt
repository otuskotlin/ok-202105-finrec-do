package com.finyou.fintrack.backend.common.context

import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction

data class ContextConfig(
    val repoProd: IRepoFinTransaction = IRepoFinTransaction.NONE,
    val repoTest: IRepoFinTransaction = IRepoFinTransaction.NONE,
)
