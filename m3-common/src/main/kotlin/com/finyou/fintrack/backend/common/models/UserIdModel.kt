package com.finyou.fintrack.backend.common.models

@JvmInline
value class UserIdModel(val id: String) {
    companion object {
        val NONE = UserIdModel("")
    }

    override fun toString() = id
}