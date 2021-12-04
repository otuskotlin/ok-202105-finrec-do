package com.finyou.fintrack.backend.common.models

interface IUserSession<T, M> {
    val fwSession: T

    suspend fun send(message: M)
}

object EmptySession: IUserSession<Unit, String> {
    override val fwSession = Unit
    override suspend fun send(message: String) {}
}
