package com.finyou.fintrack.backend.cor.common.cor


interface ICorChainDsl<T> : ICorExecDsl<T>, ICorHandlerDsl<T> {
    fun add(worker: ICorExecDsl<T>)
}

interface ICorWorkerDsl<T> : ICorExecDsl<T>, ICorHandlerDsl<T> {
    fun handle(function: T.() -> Unit)
}

interface ICorHandlerDsl<T> {
    fun on(function: T.() -> Boolean)
    fun except(function: T.(e: Throwable) -> Unit)
}

interface ICorExecDsl<T> {
    val title: String
    val description: String
    fun build(): ICorExec<T>
}

interface ICorWorker<T> : ICorExec<T> {
    override suspend fun exec(ctx: T) {
        try {
            if (on(ctx)) handle(ctx)
        } catch (e: Throwable) {
            except(ctx, e)
        }
    }

    suspend fun on(ctx: T): Boolean
    suspend fun handle(ctx: T)
    suspend fun except(ctx: T, e: Throwable)
}

interface ICorExec<T> {
    suspend fun exec(ctx: T)
}