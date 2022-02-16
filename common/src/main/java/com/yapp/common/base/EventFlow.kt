package com.yapp.common.base

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import java.util.concurrent.atomic.AtomicBoolean

/*
* [PRND(헤이딜러) EventFlow]
* https://github.com/PRNDcompany/MvvmEventSample
*/

interface EventFlow<out T> : Flow<T> {

    companion object {
        const val DEFAULT_REPLAY: Int = 3
    }
}

interface MutableEventFlow<T> : EventFlow<T>, FlowCollector<T>

fun <T> MutableEventFlow(
    replay: Int = EventFlow.DEFAULT_REPLAY
): MutableEventFlow<T> = EventFlowImpl(replay)

fun <T> MutableEventFlow<T>.asEventFlow(): EventFlow<T> = ReadOnlyEventFlow(this)

private class ReadOnlyEventFlow<T>(flow: EventFlow<T>) : EventFlow<T> by flow

private class EventFlowImpl<T>(
    replay: Int
) : MutableEventFlow<T> {

    private val flow: MutableSharedFlow<EventFlowSlot<T>> = MutableSharedFlow(replay = replay)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) = flow.collect { slot ->
        if (!slot.markConsumed()) {
            collector.emit(slot.value)
        }
    }

    override suspend fun emit(value: T) {
        flow.emit(EventFlowSlot(value))
    }
}

private class EventFlowSlot<T>(val value: T) {

    private val consumed: AtomicBoolean = AtomicBoolean(false)

    fun markConsumed(): Boolean = consumed.getAndSet(true)
}
