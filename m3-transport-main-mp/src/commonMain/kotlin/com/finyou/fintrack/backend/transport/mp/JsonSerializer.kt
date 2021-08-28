package com.finyou.fintrack.backend.transport.mp

import com.finyou.fintrack.kmp.models.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.SerializersModule

val jsonSerializer = Json {
    prettyPrint = true
    classDiscriminator = "messageType"
    serializersModule = SerializersModule {
        polymorphic(BaseMessage::class) {
            subclass(InitTransactionRequest::class, InitTransactionRequest.serializer())
            subclass(CreateTransactionRequest::class, CreateTransactionRequest.serializer())
            subclass(ReadTransactionRequest::class, ReadTransactionRequest.serializer())
            subclass(UpdateTransactionRequest::class, UpdateTransactionRequest.serializer())
            subclass(DeleteTransactionRequest::class, DeleteTransactionRequest.serializer())
            subclass(SearchTransactionRequest::class, SearchTransactionRequest.serializer())
            subclass(InitTransactionResponse::class, InitTransactionResponse.serializer())
            subclass(CreateTransactionResponse::class, CreateTransactionResponse.serializer())
            subclass(ReadTransactionResponse::class, ReadTransactionResponse.serializer())
            subclass(UpdateTransactionResponse::class, UpdateTransactionResponse.serializer())
            subclass(DeleteTransactionResponse::class, DeleteTransactionResponse.serializer())
            subclass(SearchTransactionResponse::class, SearchTransactionResponse.serializer())
        }
    }
}