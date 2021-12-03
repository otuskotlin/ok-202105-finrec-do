package com.finyou.fintrack.backend.cor.common

import com.finyou.fintrack.backend.cor.common.cor.ICorChainDsl
import com.finyou.fintrack.backend.cor.common.handlers.chain
import com.finyou.fintrack.backend.cor.common.handlers.parallel
import com.finyou.fintrack.backend.cor.common.handlers.worker
import kotlin.test.Test

class CorTest {

    @Test
    fun corTest() {
        val chain = chain<TestContext> {
            worker {
                title = "Some operation"
                description = "Description for operation"
                on { some > 0 }
                handle { some += 10 }
                except { e: Throwable -> println("Some exception: ${e.message}") }
            }

            chain {
                on { some > 0 }
                worker(
                    title = "Some other worker",
                    description = "Another way to use worker"
                ) {
                    some++
                }
            }

            parallel {
                on { some > 15 }
                worker {
                    handle { some += 10 }
                }
            }
            printResult(title = "Print result")
        }.build()
    }
}

class TestContext(var some: Int = 0)

fun ICorChainDsl<TestContext>.printResult(title: String = "", description: String = "") = worker(title, description) {
    println("Some = $some")
}
