package com.yapp.presentation.ui.member.qrcodescanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.TimeUnit

class QrCodeAnalyzer(
    private val onQrCodeDetected: (barcodes: List<Barcode>) -> Unit,
    private val onFailToAnalysis: (exception: Exception) -> Unit
) : ImageAnalysis.Analyzer {
    private val SCAN_DURATION = 3L
    private var lastAnalyzedTimeStamp = 0L

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val currentTimeStamp = System.currentTimeMillis()
        if (currentTimeStamp - lastAnalyzedTimeStamp >= TimeUnit.SECONDS.toMillis(SCAN_DURATION)) {
            imageProxy.image?.let { imageToAnalyze ->
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
                val qrCodeScanner = BarcodeScanning.getClient(options)
                val imageToProcess =
                    InputImage.fromMediaImage(imageToAnalyze, imageProxy.imageInfo.rotationDegrees)

                qrCodeScanner.process(imageToProcess)
                    .addOnSuccessListener { qrCodes ->
                        if (qrCodes.isNotEmpty()) {
                            onQrCodeDetected(qrCodes)
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailToAnalysis(exception)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }
            lastAnalyzedTimeStamp = currentTimeStamp
        } else {
            imageProxy.close()
        }
    }
}
