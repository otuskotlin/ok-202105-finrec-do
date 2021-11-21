package com.finyou.fintrack.backend.validation

import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.validation.cor.workers.validation
import com.finyou.fintrack.backend.validation.validators.ValidatorStringNotEmpty
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking

class ValidationTest : StringSpec( {

    "Pipeline validation" {
        val chain = chain<TestContext> {
            validation {
                errorHandler { result ->
                    if (!result.isSuccess) errors.addAll(result.errors)
                }
                validate<String?> {
                    validator(ValidatorStringNotEmpty(field = "firstArg"))
                    on { firstArg }
                }
                validate<String?> {
                    validator(ValidatorStringNotEmpty(field = "secondArg"))
                    on { secondArg }
                }
            }
        }

        val c = TestContext()

        runBlocking {
            chain.build().exec(c)
            c.errors.forEach { println(it) }
            c.errors.size shouldBe 2
        }
    }

})


data class TestContext(
    val firstArg: String = "",
    val secondArg: String = "",
    val errors: MutableList<IValidationError> = mutableListOf()
)