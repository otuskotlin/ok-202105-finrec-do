package com.finyou.fintrack.backend.logging

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import java.time.Instant

suspend fun <T> Logger.wrapWithLogging(
    id: String = "",
    block: suspend () -> T,
): T = try {
    val timeStart = Instant.now()
    info("Entering $id")
    block().also {
        val diffTime = Instant.now().toEpochMilli() - timeStart.toEpochMilli()
        info("Finishing $id", Pair("metricHandleTime", diffTime))
    }
} catch (e: Throwable) {
    error("Failing $id", e)
    throw e
}

fun ftLogger(loggerId: String): FtLogWrapper = ftLogger(
    logger = LoggerFactory.getLogger(loggerId) as Logger
)

fun ftLogger(cls: Class<out Any>): FtLogWrapper = ftLogger(
    logger = LoggerFactory.getLogger(cls) as Logger
)

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun ftLogger(logger: Logger): FtLogWrapper = FtLogWrapper(
    logger = logger,
    loggerId = logger.name,
)
