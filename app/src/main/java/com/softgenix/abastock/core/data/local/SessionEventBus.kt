package com.softgenix.abastock.core.data.local

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<SessionEvent>()
    val events = _events.asSharedFlow()

    suspend fun emit(event: SessionEvent) {
        _events.emit(event)
    }
}

sealed class SessionEvent {
    object Logout : SessionEvent()
}
