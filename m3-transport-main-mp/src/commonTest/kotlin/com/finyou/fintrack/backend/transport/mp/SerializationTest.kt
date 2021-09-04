package com.finyou.fintrack.backend.transport.mp

import com.finyou.fintrack.kmp.models.BaseMessage
import com.finyou.fintrack.kmp.models.TransactionType
import com.finyou.fintrack.kmp.models.UpdatableTransaction
import com.finyou.fintrack.kmp.models.UpdateTransactionRequest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {
    private val requestId = "123456"
    private val createTransactionRequest = UpdateTransactionRequest(
        requestId = requestId,
        updateTransaction = UpdatableTransaction(
            userId = "987654",
            name = "Coffee",
            date = "2021-08-22T15:10:00Z",
            transactionType = TransactionType.OUTCOME,
            amount = 2.75,
            currency = "USD",
            id = "325896"
        )
    )

    @Test
    fun testSerialize() {
        val json = Json { prettyPrint = true }
        val serialized = json.encodeToString(createTransactionRequest)
        println("serialized = $serialized")
        assertTrue { serialized.contains(TransactionType.OUTCOME.value) }
    }

    @Test
    fun testDeserialize() {
        val json = jsonSerializer
        val request: BaseMessage = createTransactionRequest
        val serialized = json.encodeToString(request)
        println("serialized = $serialized")

        val deserializedObject = json.decodeFromString<BaseMessage>(serialized)
        assertEquals("325896", (deserializedObject as UpdateTransactionRequest).updateTransaction?.id)
    }

}