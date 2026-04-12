package com.softgenix.abastock.core.hardware.domain

interface VibrationManager {
    fun vibrateSuccess()
    fun vibrateError()
    fun vibrateClick()
}