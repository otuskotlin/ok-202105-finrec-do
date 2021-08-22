package com.finyou.fintrack.backend.models

@JvmInline
value class UserIdModel(val id: String) {
    companion object {
        val NONE = UserIdModel("")
    }
}