package com.finyou.fintrack.backend.common.models

@JvmInline
value class FinTransactionIdModel(val id: String) {
    companion object {
        val NONE = FinTransactionIdModel("")
    }
}