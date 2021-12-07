package com.finyou.fintrack.backend.repo.test

import com.finyou.fintrack.backend.common.models.ErrorModel
import com.finyou.fintrack.backend.common.models.FinTransactionIdModel
import com.finyou.fintrack.backend.common.models.FinTransactionModel
import com.finyou.fintrack.backend.repo.common.DbFinTransactionIdRequest
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

abstract class RepoFinTransactionDeleteTest {
    abstract val repo: IRepoFinTransaction

    @Test
    fun deleteSuccess() {
        val result = runBlocking { repo.delete(DbFinTransactionIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(deleteSuccessStub, result.result)
        assertEquals(emptyList<ErrorModel>(), result.errors)
    }

    @Test
    fun deleteNotFound() {
        val result = runBlocking { repo.read(DbFinTransactionIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(ErrorModel(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object: BaseInitFinTransactions() {
        override val initObjects: List<FinTransactionModel> = listOf(
            createInitTestModel("delete")
        )
        private val deleteSuccessStub = initObjects.first()
        val successId = deleteSuccessStub.id
        val notFoundId = FinTransactionIdModel(UUID.randomUUID().toString())
    }
}