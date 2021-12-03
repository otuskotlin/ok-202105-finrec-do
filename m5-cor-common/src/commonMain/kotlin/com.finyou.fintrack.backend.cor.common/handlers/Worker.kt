package com.finyou.fintrack.backend.cor.common.handlers

import com.finyou.fintrack.backend.cor.common.cor.CorDslMarker
import com.finyou.fintrack.backend.cor.common.cor.ICorExec
import com.finyou.fintrack.backend.cor.common.cor.ICorWorker
import com.finyou.fintrack.backend.cor.common.cor.ICorWorkerDsl

@CorDslMarker
class CorWorkerDsl<T>(
    override var title: String = "",
    override var description: String = "",
    private var blockOn: T.() -> Boolean = { true },
    private var blockHandle: T.() -> Unit = {},
    private var blockExcept: T.(Throwable) -> Unit = { }
) : ICorWorkerDsl<T> {
    override fun on(function: T.() -> Boolean) {
        blockOn = function
    }

    override fun handle(function: T.() -> Unit) {
        blockHandle = function
    }

    override fun except(function: T.(Throwable) -> Unit) {
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
    val blockOn: T.() -> Boolean,
    val blockHandle: T.() -> Unit,
    val blockExcept: T.(Throwable) -> Unit
) : ICorWorker<T> {
    override suspend fun on(ctx: T): Boolean = blockOn(ctx)
    override suspend fun handle(ctx: T) = blockHandle(ctx)
    override suspend fun except(ctx: T, e: Throwable) = blockExcept(ctx, e)
}