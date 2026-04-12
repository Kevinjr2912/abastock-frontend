package com.softgenix.abastock.core.hardware.data


import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.softgenix.abastock.core.hardware.domain.ScannerManager
import javax.inject.Inject

class AndroidScannerManager @Inject constructor() : ScannerManager {

    private val scanner = BarcodeScanning.getClient()

    override fun getAnalyzer(onBarcodeDetected: (String) -> Unit): ImageAnalysis.Analyzer {
        return BarcodeAnalyzer(onBarcodeDetected)
    }

    private inner class BarcodeAnalyzer(
        private val onBarcodeDetected: (String) -> Unit
    ) : ImageAnalysis.Analyzer {

        @OptIn(ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            barcode.rawValue?.let { code ->
                                onBarcodeDetected(code)
                            }
                        }
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }
    }
}