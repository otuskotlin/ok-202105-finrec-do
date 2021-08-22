package com.finyou.fintrack.backend.models

@JvmInline
value class TransactionIdModel(val id: String) {
    companion object {
        val NONE = TransactionIdModel("")
    }
}