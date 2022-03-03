package com.yapp.presentation.ui.member.qrcodescan

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.TimeUnit

class QrCodeAnalyzer(
    private val onQrCodeDetected: (barcodes: List<Barcode>) -> Unit,
) : ImageAnalysis.Analyzer {
    private var lastAnalyzedTimeStamp = 0L

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val currentTimeStamp = System.currentTimeMillis()
        if (currentTimeStamp - lastAnalyzedTimeStamp >= TimeUnit.SECONDS.toMillis(1)) {
            imageProxy.image?.let { imageToAnalyze ->
                //imageToAnalyze.cropRect = readableRectArea
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
                        } else {
                            Log.d("QrCodeAnalyzer", "QrCodeAnalyzer: No barcode Scanned")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("QrCodeAnalyzer", "QrCodeAnalyzer: exception $exception")
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
