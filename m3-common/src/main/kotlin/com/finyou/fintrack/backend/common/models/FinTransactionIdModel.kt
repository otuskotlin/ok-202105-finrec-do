package com.finyou.fintrack.backend.common.models

import java.util.*

@JvmInline
value class FinTransactionIdModel(val id: String) {
    companion object {
        val NONE = FinTransactionIdModel("")
    }

    override fun toString() = id

    val toUUID: UUID
        get() = UUID.fromString(id)
}