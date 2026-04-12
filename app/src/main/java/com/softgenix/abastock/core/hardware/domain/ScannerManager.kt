package com.softgenix.abastock.core.hardware.domain


import androidx.camera.core.ImageAnalysis

interface ScannerManager {

    fun getAnalyzer(onBarcodeDetected: (String) -> Unit): ImageAnalysis.Analyzer
}