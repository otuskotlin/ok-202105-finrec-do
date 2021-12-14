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

abstract class RepoFinTransactionReadTest {
    abstract val repo: IRepoFinTransaction

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.read(DbFinTransactionIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub, result.result)
        assertEquals(emptyList<ErrorModel>(), result.errors)
    }

    @Test
    fun readNotFound() {
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
            createInitTestModel("read")
        )
        private val readSuccessStub = initObjects.first()

        val successId = readSuccessStub.id
        val notFoundId = FinTransactionIdModel(UUID.randomUUID().toString())

    }
}