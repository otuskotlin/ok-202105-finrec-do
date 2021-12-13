package com.finyou.fintrack.backend.repo.psql

import com.finyou.fintrack.backend.common.models.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.math.BigDecimal
import java.time.Instant

object FinTransactionTable: Table("fin_transactions") {
    val id = uuid("id").uniqueIndex().autoGenerate()
    val userId = varchar("user_id", 128)
    val name = varchar("name", 128)
    val description = varchar("description", 256)
    val date = long("date")
    val transactionType = enumeration("transaction_type", TypeModel::class)
    val amount = double("amount")
    val currency = varchar("currency", 128)
    val permissions = varchar("permissions", 256)

    override val primaryKey = PrimaryKey(id)

    fun from(entity: InsertStatement<Number>) = FinTransactionModel(
        id = FinTransactionIdModel(entity[id].toString()),
        userId = UserIdModel(entity[userId]),
        name = entity[name],
        description = entity[description],
        date = Instant.ofEpochMilli(entity[date]),
        transactionType = entity[transactionType],
        amountCurrency = AmountCurrencyModel(
            amount = BigDecimal.valueOf(entity[amount]),
            currency = entity[currency]
        ),
        permissions = entity[permissions].split(", ").map { PermissionModel.valueOf(it) }.toMutableSet()
    )

    fun from(entity: ResultRow) = FinTransactionModel(
        id = FinTransactionIdModel(entity[id].toString()),
        userId = UserIdModel(entity[userId]),
        name = entity[name],
        description = entity[description],
        date = Instant.ofEpochMilli(entity[date]),
        transactionType = entity[transactionType],
        amountCurrency = AmountCurrencyModel(
            amount = BigDecimal.valueOf(entity[amount]),
            currency = entity[currency]
        ),
        permissions = entity[permissions].split(", ").map { PermissionModel.valueOf(it) }.toMutableSet()
    )
}