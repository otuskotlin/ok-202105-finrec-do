package com.finyou.fintrack.backend.validation.cor.workers

import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.worker
import com.finyou.fintrack.backend.validation.cor.ValidationBuilder

fun <C> ICorChainDsl<C>.validation(block: ValidationBuilder<C>.() -> Unit) {
    worker {
        this.title = "Logic validation"
        handle {
            ValidationBuilder<C>().apply(block).build().exec(this)
        }
    }
}