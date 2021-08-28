package com.finyou.fintrack.backend.common.models

@JvmInline
value class TransactionIdModel(val id: String) {
    companion object {
        val NONE = TransactionIdModel("")
    }
}