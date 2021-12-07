package com.finyou.fintrack.backend.repo.test

import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.repo.common.DbFinTransactionModelRequest
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.time.Instant
import java.util.*

abstract class RepoFinTransactionCreateTest {
    abstract val repo: IRepoFinTransaction

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.create(DbFinTransactionModelRequest(createObj)) }
        val expected = createObj.copy(id = result.result?.id ?: FinTransactionIdModel.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result)
        assertNotEquals(FinTransactionIdModel.NONE, result.result?.id)
        assertEquals(emptyList<ErrorModel>(), result.errors)
    }

    companion object: BaseInitFinTransactions() {

        private val createObj = FinTransactionModel(
            userId = UserIdModel(UUID.randomUUID().toString()),
            name = "create transaction",
            description = "create transaction description",
            date = Instant.now().minusSeconds(50000L),
            transactionType = TypeModel.INCOME,
            amountCurrency = AmountCurrencyModel(
                amount = 2000.toBigDecimal(),
                currency = "USD"
            ),
            permissions = mutableSetOf(PermissionModel.READ, PermissionModel.UPDATE)
        )
        override val initObjects: List<FinTransactionModel> = emptyList()
    }
}