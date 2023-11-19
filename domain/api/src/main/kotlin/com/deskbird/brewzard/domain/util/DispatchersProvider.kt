package com.deskbird.brewzard.domain.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val immediate: CoroutineDispatcher

    companion object {
        fun testDispatchersProvider(dispatcher: CoroutineDispatcher) =
            object : DispatchersProvider {
                override val io: CoroutineDispatcher = dispatcher
                override val default: CoroutineDispatcher = dispatcher
                override val main: CoroutineDispatcher = dispatcher
                override val immediate: CoroutineDispatcher = dispatcher
            }
    }
}
