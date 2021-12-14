package com.finyou.fintrack.backend.common.utils

import java.time.Instant

fun <T1 : Any, T2 : Any, R : Any> doubleLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline val Instant.dateToMillis: Long
    get() = when(this) {
        Instant.MIN  -> Long.MIN_VALUE
        Instant.MAX -> Long.MAX_VALUE
        else -> this.toEpochMilli()
    }