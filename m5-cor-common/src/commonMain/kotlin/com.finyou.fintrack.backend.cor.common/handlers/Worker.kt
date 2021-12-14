package com.finyou.fintrack.backend.cor.common.handlers

import com.finyou.fintrack.backend.cor.common.cor.CorDslMarker
import com.finyou.fintrack.backend.cor.common.cor.ICorExec
import com.finyou.fintrack.backend.cor.common.cor.ICorWorker
import com.finyou.fintrack.backend.cor.common.cor.ICorWorkerDsl

@CorDslMarker
class CorWorkerDsl<T>(
    override var title: String = "",
    override var description: String = "",
    private var blockOn: suspend  T.() -> Boolean = { true },
    private var blockHandle: suspend  T.() -> Unit = {},
    private var blockExcept: suspend  T.(Throwable) -> Unit = { }
) : ICorWorkerDsl<T> {
    override fun on(function: suspend  T.() -> Boolean) {
        blockOn = function
    }

    override fun handle(function: suspend  T.() -> Unit) {
        blockHandle = function
    }

    override fun except(function: suspend  T.(Throwable) -> Unit) {
        blockExcept = function
    }

    override fun build(): ICorExec<T> = CorWorker<T>(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )
}

class CorWorker<T>(
    val title: String,
    val description: String,
    val blockOn: suspend  T.() -> Boolean,
    val blockHandle: suspend  T.() -> Unit,
    val blockExcept: suspend  T.(Throwable) -> Unit
) : ICorWorker<T> {
    override suspend fun on(ctx: T): Boolean = blockOn(ctx)
    override suspend fun handle(ctx: T) = blockHandle(ctx)
    override suspend fun except(ctx: T, e: Throwable) = blockExcept(ctx, e)
}