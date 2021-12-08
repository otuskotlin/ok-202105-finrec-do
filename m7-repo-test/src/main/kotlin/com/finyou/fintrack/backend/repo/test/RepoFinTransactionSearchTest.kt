package com.finyou.fintrack.backend.repo.test

import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.repo.common.DbFinTransactionFilterRequest
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

abstract class RepoFinTransactionSearchTest {
    abstract val repo: IRepoFinTransaction

    @Test
    fun searchOwner() {
        val result = runBlocking { repo.search(DbFinTransactionFilterRequest(userId = searchUserId)) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[1], initObjects[3])
        assertEquals(expected.sortedBy { it.id.toString() }, result.result.sortedBy { it.id.toString() })
        assertEquals(emptyList<ErrorModel>(), result.errors)
    }

    @Test
    fun searchDealSide() {
        val result = runBlocking { repo.search(DbFinTransactionFilterRequest(filter = FilterModel(transactionType = searchTransactionType))) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[2], initObjects[4])
        assertEquals(expected.sortedBy { it.id.toString() }, result.result.sortedBy { it.id.toString() })
        assertEquals(emptyList<ErrorModel>(), result.errors)
    }

    @Test
    fun searchStr() {
        val result = runBlocking { repo.search(DbFinTransactionFilterRequest(searchStr = "transaction1")) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[0])
        assertEquals(expected.sortedBy { it.id.toString() }, result.result.sortedBy { it.id.toString() })
        assertEquals(emptyList<ErrorModel>(), result.errors)
    }

    companion object : BaseInitFinTransactions() {

        val searchUserId = UserIdModel(UUID.randomUUID().toString())
        val searchTransactionType = TypeModel.OUTCOME
        override val initObjects: List<FinTransactionModel> = listOf(
            createInitTestModel("transaction1"),
            createInitTestModel("transaction2", userId = searchUserId),
            createInitTestModel("transaction3", transactionType = searchTransactionType),
            createInitTestModel("transaction4", userId = searchUserId),
            createInitTestModel("transaction5", transactionType = searchTransactionType),
        )
    }
}