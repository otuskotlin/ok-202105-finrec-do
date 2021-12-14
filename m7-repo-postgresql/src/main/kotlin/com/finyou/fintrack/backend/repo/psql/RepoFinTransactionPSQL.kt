package com.finyou.fintrack.backend.repo.psql

import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.repo.common.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.transactions.transaction

class RepoFinTransactionPSQL(
    url: String = "jdbc:postgresql://localhost:5432/fintrackdevdb",
    user: String = "fintrack",
    password: String = "finyou",
    schema: String = "fintrack",
    private val initObjects: List<FinTransactionModel> = listOf(),
): IRepoFinTransaction {

    private val db by lazy { SqlConnector(url, user, password, schema).connect(FinTransactionTable) }

    init {
        runBlocking { initObjects.forEach { save(it) } }
    }

    private fun <T> safeTransaction(
        statement: Transaction.() -> T,
        handleErrors: Exception.() -> T
    ): T {
        return try {
            transaction(db, statement)
        } catch (e: Exception) {
            handleErrors(e)
        }
    }

    private suspend fun save(item: FinTransactionModel): DbFinTransactionResponse {
        return safeTransaction({
            val result = FinTransactionTable.insert {
                if (item.id != FinTransactionIdModel.NONE) {
                    it[id] = item.id.toUUID
                }
                it[userId] = item.userId.toString()
                it[name] = item.name
                it[description] = item.description
                it[date] = item.date.toEpochMilli()
                it[transactionType] = item.transactionType
                it[amount] = item.amountCurrency.amount.toDouble()
                it[currency] = item.amountCurrency.currency
                it[permissions] = item.permissions.joinToString { perm -> perm.name }
            }
            DbFinTransactionResponse(
                isSuccess = true,
                result = FinTransactionTable.from(result)
            )
        },
            {
                println("save ${this.localizedMessage}")
                DbFinTransactionResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(ErrorModel(message = this.localizedMessage))
                )
            })
    }

    override suspend fun create(rq: DbFinTransactionModelRequest): DbFinTransactionResponse {
        return save(rq.finTransaction)
    }

    override suspend fun read(rq: DbFinTransactionIdRequest): DbFinTransactionResponse {
        return safeTransaction(
            {
                val result = FinTransactionTable.select {
                    FinTransactionTable.id eq rq.id.toUUID
                }.single()
                DbFinTransactionResponse(
                    isSuccess = true,
                    result = FinTransactionTable.from(result)
                )
            },
            {
                println("read ${this.localizedMessage}")
                DbFinTransactionResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(notFoundIdError)
                )
            }
        )
    }

    override suspend fun update(rq: DbFinTransactionModelRequest): DbFinTransactionResponse {
        return safeTransaction({
            FinTransactionTable.update({ FinTransactionTable.id eq rq.finTransaction.id.toUUID }) {
                it[userId] = rq.finTransaction.userId.toString()
                it[name] = rq.finTransaction.name
                it[description] = rq.finTransaction.description
                it[date] = rq.finTransaction.date.toEpochMilli()
                it[transactionType] = rq.finTransaction.transactionType
                it[amount] = rq.finTransaction.amountCurrency.amount.toDouble()
                it[currency] = rq.finTransaction.amountCurrency.currency
                it[permissions] = rq.finTransaction.permissions.joinToString { perm -> perm.name }
            }
            val result = FinTransactionTable.select {
                FinTransactionTable.id eq rq.finTransaction.id.toUUID
            }.single()
            DbFinTransactionResponse(
                isSuccess = true,
                result = FinTransactionTable.from(result)
            )
        },
            {
                println("update ${this.localizedMessage}")
                DbFinTransactionResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(notFoundIdError)
                )
            })
    }

    override suspend fun delete(rq: DbFinTransactionIdRequest): DbFinTransactionResponse {
        return safeTransaction(
            {
                val result = FinTransactionTable.select {
                    FinTransactionTable.id eq rq.id.toUUID
                }.single()
                FinTransactionTable.deleteWhere {
                    FinTransactionTable.id eq rq.id.toUUID
                }
                DbFinTransactionResponse(
                    isSuccess = true,
                    result = FinTransactionTable.from(result)
                )
            },
            {
                println("delete ${this.localizedMessage}")
                DbFinTransactionResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(notFoundIdError)
                )
            }
        )
    }

    private val notFoundIdError by lazy { ErrorModel(field = "id", message = "Not Found") }

    private infix fun <T, S : T?> ExpressionWithColumnType<S>.dateBetween(range: LongRange) =
        between(range.first, range.last)

    private infix fun <T, S : T?> ExpressionWithColumnType<S>.amountBetween(filter: FilterModel) =
        between(filter.amountFrom.toDouble(), filter.amountTo.toDouble())


    override suspend fun search(rq: DbFinTransactionFilterRequest): DbFinTransactionsResponse {
        return safeTransaction(
            {
                val result = FinTransactionTable.select {
                    (if (rq.userId == UserIdModel.NONE) Op.TRUE
                    else FinTransactionTable.userId eq rq.userId.toString()) and
                            (if (rq.filter == FilterModel.NONE) Op.TRUE else {
                                (FinTransactionTable.date dateBetween rq.filter.dateRange) and
                                        (FinTransactionTable.amount amountBetween rq.filter) and
                                        (if (rq.filter.transactionType == TypeModel.NONE) Op.TRUE
                                        else FinTransactionTable.transactionType eq rq.filter.transactionType)
                            })
                }
                DbFinTransactionsResponse(
                    isSuccess = true,
                    result = result.map { FinTransactionTable.from(it) }
                )
            },
            {
                println("search ${this.localizedMessage}")
                printStackTrace()
                DbFinTransactionsResponse(
                    result = emptyList(),
                    isSuccess = false,
                    errors = listOf(ErrorModel(message = localizedMessage))
                )
            }
        )
    }
}