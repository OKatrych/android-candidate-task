package com.nordlocker.domain.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.retryWhen

fun <T> Flow<T>.retryWithPolicy(
    retryPolicy: RetryPolicy,
    predicate: suspend FlowCollector<T>.(cause: Throwable) -> Boolean
): Flow<T> {
    var currentDelay = retryPolicy.delayMillis
    val delayFactor = retryPolicy.delayFactor
    return retryWhen { cause, attempt ->
        if (predicate(cause) && attempt < retryPolicy.numRetries) {
            delay(currentDelay)
            currentDelay = (currentDelay * delayFactor).coerceAtMost(retryPolicy.maxDelayMillis)
            return@retryWhen true
        } else {
            return@retryWhen false
        }
    }
}