package com.finyou.fintrack.backend.cor.common.handlers

import com.finyou.fintrack.backend.cor.common.cor.CorDslMarker
import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl


fun <T> chain(function: CorChainDsl<T>.() -> Unit) = CorChainDsl<T>().apply(function)

@CorDslMarker
fun <T> ICorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

@CorDslMarker
fun <T> ICorChainDsl<T>.parallel(function: CorParallelDsl<T>.() -> Unit) {
    add(CorParallelDsl<T>().apply(function))
}

@CorDslMarker
fun <T> ICorChainDsl<T>.worker(function: CorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(function))
}

@CorDslMarker
fun <T> ICorChainDsl<T>.worker(title: String, description: String, function: suspend  T.() -> Unit) {
    add(
        CorWorkerDsl<T>(
            title = title,
            description = description,
            blockHandle = function
        )
    )
}