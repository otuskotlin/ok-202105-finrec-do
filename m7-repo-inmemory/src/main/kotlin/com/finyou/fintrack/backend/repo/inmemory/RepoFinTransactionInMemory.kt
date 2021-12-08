package com.finyou.fintrack.backend.repo.inmemory


import com.finyou.fintrack.backend.common.models.*
import org.ehcache.Cache
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import com.finyou.fintrack.backend.repo.common.*
import com.finyou.fintrack.backend.repo.inmemory.models.FinTransactionRow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.util.*

class RepoFinTransactionInMemory(
    private val initObjects: List<FinTransactionModel> = listOf(),
    private val ttl: Duration = Duration.ofMinutes(1)
) : IRepoFinTransaction {

    private val cacheManager by lazy {
        CacheManagerBuilder
            .newCacheManagerBuilder()
            .build(true)
    }

    private val cache: Cache<String, FinTransactionRow> by lazy {
        cacheManager.createCache(
            "finyou-fin-transaction-cache",
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                    String::class.java,
                    FinTransactionRow::class.java,
                    ResourcePoolsBuilder.heap(100)
                )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
                .build()
        )
    }

    init {
        runBlocking { initObjects.forEach {
            save(it)
        } }
    }

    private suspend fun save(item: FinTransactionModel): DbFinTransactionResponse {
        val row = FinTransactionRow(item)
        if (row.id == null)
            return getDbFailedResponse(field = "id", message = "Id must not be null or blank")

        cache.put(row.id, row)
        return DbFinTransactionResponse(
            result = row.toInternal(),
            isSuccess = true
        )
    }

    override suspend fun create(rq: DbFinTransactionModelRequest): DbFinTransactionResponse =
        save(rq.finTransaction.copy(id = FinTransactionIdModel(UUID.randomUUID().toString())))

    override suspend fun read(rq: DbFinTransactionIdRequest): DbFinTransactionResponse =
        cache.get(rq.id.toString())?.let {
            DbFinTransactionResponse(
                result = it.toInternal(),
                isSuccess = true
            )
        } ?: getDbFailedResponse(field = "id", message = "Not Found")

    override suspend fun update(rq: DbFinTransactionModelRequest): DbFinTransactionResponse {
        val key = rq.finTransaction.id.takeIf { it != FinTransactionIdModel.NONE }?.toString()
            ?: return getDbFailedResponse(field = "id", message = "Id must not be null or blank")

        return if (cache.containsKey(key)) {
            save(rq.finTransaction)
            DbFinTransactionResponse(
                result = rq.finTransaction,
                isSuccess = true
            )
        } else getDbFailedResponse(field = "id", message = "Not Found")
    }

    override suspend fun delete(rq: DbFinTransactionIdRequest): DbFinTransactionResponse {
        val key = rq.id.takeIf { it != FinTransactionIdModel.NONE }?.toString()
            ?: return getDbFailedResponse(field = "id", message = "Id must not be null or blank")
        val row = cache.get(key)
            ?: return getDbFailedResponse(field = "id", message = "Not Found")
        cache.remove(key)
        return DbFinTransactionResponse(
            result = row.toInternal(),
            isSuccess = true,
        )
    }

    override suspend fun search(rq: DbFinTransactionFilterRequest): DbFinTransactionsResponse {
        val results = cache.asFlow()
            .filter {
                if (rq.userId == UserIdModel.NONE) return@filter true
                rq.userId.toString() == it.value.userId
            }
            .filter {
                if (rq.filter.transactionType == TypeModel.NONE) return@filter true
                rq.filter.transactionType.name == it.value.transactionType
            }.filter {
                if (rq.searchStr.length < 4) return@filter true
                it.value.toString().contains(rq.searchStr)
            }
            .map { it.value.toInternal() }
            .toList()
        return DbFinTransactionsResponse(
            result = results,
            isSuccess = true
        )
    }

    private fun getDbFailedResponse(message: String, field: String): DbFinTransactionResponse {
        return DbFinTransactionResponse(
            result = null,
            isSuccess = false,
            errors = listOf(ErrorModel(message, field))
        )
    }
}