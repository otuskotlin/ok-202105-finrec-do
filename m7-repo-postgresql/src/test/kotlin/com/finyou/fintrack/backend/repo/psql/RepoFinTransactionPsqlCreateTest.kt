package com.finyou.fintrack.backend.repo.psql

import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction
import com.finyou.fintrack.backend.repo.test.*

class RepoFinTransactionPsqlCreateTest : RepoFinTransactionCreateTest() {
    override val repo: IRepoFinTransaction =
        SqlTestContainer.repoWithTestContainer(initObjects)
}

class RepoFinTransactionPsqlReadTest : RepoFinTransactionReadTest() {
    override val repo: IRepoFinTransaction =
        SqlTestContainer.repoWithTestContainer(initObjects)
}

class RepoFinTransactionPsqlUpdateTest : RepoFinTransactionUpdateTest() {
    override val repo: IRepoFinTransaction =
        SqlTestContainer.repoWithTestContainer(initObjects)
}

class RepoFinTransactionPsqlDeleteTest : RepoFinTransactionDeleteTest() {
    override val repo: IRepoFinTransaction =
        SqlTestContainer.repoWithTestContainer(initObjects)
}

class RepoFinTransactionPsqlSearchTest : RepoFinTransactionSearchTest() {
    override val repo: IRepoFinTransaction =
        SqlTestContainer.repoWithTestContainer(initObjects)
}