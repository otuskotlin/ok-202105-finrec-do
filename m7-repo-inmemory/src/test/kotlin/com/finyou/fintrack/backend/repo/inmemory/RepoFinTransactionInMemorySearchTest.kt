package com.finyou.fintrack.backend.repo.inmemory

import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction
import com.finyou.fintrack.backend.repo.test.RepoFinTransactionSearchTest

class RepoFinTransactionInMemorySearchTest : RepoFinTransactionSearchTest() {
    override val repo: IRepoFinTransaction = RepoFinTransactionInMemory(
        initObjects = initObjects
    )
}