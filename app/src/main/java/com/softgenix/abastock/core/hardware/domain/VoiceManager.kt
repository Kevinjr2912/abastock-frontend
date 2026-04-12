package com.softgenix.abastock.core.hardware.domain

interface VoiceManager {
    fun startListening(onResult: (String) -> Unit)
    fun stopListening()
    fun isAvailable(): Boolean
}