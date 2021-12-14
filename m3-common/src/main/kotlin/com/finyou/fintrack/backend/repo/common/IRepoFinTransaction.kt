package com.finyou.fintrack.backend.repo.common

interface IRepoFinTransaction {
    suspend fun create(rq: DbFinTransactionModelRequest): DbFinTransactionResponse
    suspend fun read(rq: DbFinTransactionIdRequest): DbFinTransactionResponse
    suspend fun update(rq: DbFinTransactionModelRequest): DbFinTransactionResponse
    suspend fun delete(rq: DbFinTransactionIdRequest): DbFinTransactionResponse
    suspend fun search(rq: DbFinTransactionFilterRequest): DbFinTransactionsResponse

    object NONE : IRepoFinTransaction {
        override suspend fun create(rq: DbFinTransactionModelRequest): DbFinTransactionResponse {
            TODO("Not yet implemented")
        }

        override suspend fun read(rq: DbFinTransactionIdRequest): DbFinTransactionResponse {
            TODO("Not yet implemented")
        }

        override suspend fun update(rq: DbFinTransactionModelRequest): DbFinTransactionResponse {
            TODO("Not yet implemented")
        }

        override suspend fun delete(rq: DbFinTransactionIdRequest): DbFinTransactionResponse {
            TODO("Not yet implemented")
        }

        override suspend fun search(rq: DbFinTransactionFilterRequest): DbFinTransactionsResponse {
            TODO("Not yet implemented")
        }
    }
}