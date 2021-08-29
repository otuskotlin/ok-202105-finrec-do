package com.finyou.fintrack.backend.common.models

data class ErrorModel(
    val message: String = "",
    val field: String = ""
) {
    companion object {
        val NONE = ErrorModel()
    }
}
