package com.finyou.fintrack.backend.cor.common.handlers

import com.finyou.fintrack.backend.cor.common.cor.*

@CorDslMarker
class CorChainDsl<T>(
    override var title: String = "",
    override var description: String = "",
    private val workers: MutableList<ICorExecDsl<T>> = mutableListOf(),
    private var blockOn: suspend T.() -> Boolean = { true },
    private var blockExcept: suspend T.(Throwable) -> Unit = { }
) : ICorChainDsl<T> {

    override fun on(function: suspend T.() -> Boolean) {
        blockOn = function
    }

    override fun except(function: suspend T.(Throwable) -> Unit) {
        blockExcept = function
    }

    override fun build() = CorChain<T>(
        title = title,
        description = description,
        blockOn = blockOn,
        blockExcept = blockExcept,
        workers = workers.map { it.build() }
    )

    override fun add(worker: ICorExecDsl<T>) {
        workers.add(worker)
    }
}

class CorChain<T>(
    val title: String,
    val description: String,
    val workers: List<ICorExec<T>>,
    val blockOn: suspend T.() -> Boolean,
    val blockExcept: suspend T.(Throwable) -> Unit
) : ICorWorker<T> {
    override suspend fun on(ctx: T): Boolean = blockOn(ctx)
    override suspend fun except(ctx: T, e: Throwable) = blockExcept(ctx, e)
    override suspend fun handle(ctx: T) {
        workers.forEach {
            it.exec(ctx)
        }
    }
}