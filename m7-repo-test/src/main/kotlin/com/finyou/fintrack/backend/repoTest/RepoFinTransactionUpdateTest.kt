package com.finyou.fintrack.backend.repoTest

import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.repo.common.DbFinTransactionModelRequest
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.util.*

abstract class RepoFinTransactionUpdateTest {
    abstract val repo: IRepoFinTransaction

    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.update(DbFinTransactionModelRequest(updateObj)) }
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj, result.result)
        assertEquals(emptyList<ErrorModel>(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.update(DbFinTransactionModelRequest(updateObjNotFound)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(listOf(ErrorModel(field = "id", message = "Not Found")), result.errors)
    }

    companion object: BaseInitFinTransactions() {
        override val initObjects: List<FinTransactionModel> = listOf(
            createInitTestModel("update")
        )
        private val updateId = initObjects.first().id
        private val updateIdNotFound = FinTransactionIdModel(UUID.randomUUID().toString())

        private val updateObj = FinTransactionModel(
            id = updateId,
            userId = UserIdModel(UUID.randomUUID().toString()),
            name = "update object",
            description = "update object description",
            date = Instant.now().minusSeconds(50000L),
            transactionType = TypeModel.OUTCOME,
            amountCurrency = AmountCurrencyModel(
                amount = 1000.toBigDecimal(),
                currency = "USD"
            ),
            permissions = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE)
        )

        private val updateObjNotFound = FinTransactionModel(
            id = updateIdNotFound,
            userId = UserIdModel(UUID.randomUUID().toString()),
            name = "update object not found",
            description = "update object not found description",
            date = Instant.now().minusSeconds(50000L),
            transactionType = TypeModel.OUTCOME,
            amountCurrency = AmountCurrencyModel(
                amount = 1000.toBigDecimal(),
                currency = "USD"
            ),
            permissions = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE)
        )
    }
}