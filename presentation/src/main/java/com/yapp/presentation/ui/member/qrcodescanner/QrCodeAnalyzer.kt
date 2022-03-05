package com.yapp.presentation.ui.member.qrcodescanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import java.nio.channels.Channel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface QrCodeAnalyzerInterface

object QrStateProvider {
    @OptIn(ObsoleteCoroutinesApi::class)
    internal val qrState = ConflatedBroadcastChannel<Boolean>()
}

class QrCodeAnalyzer @Inject constructor(
    val qrStateProvider: QrStateProvider
): ImageAnalysis.Analyzer, QrCodeAnalyzerInterface {
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
                            qrStateProvider.qrState.send(true)
                         //   onQrCodeDetected(qrCodes)
                        }
                    }
                    .addOnFailureListener { exception ->
                        qrStateProvider.qrState.send(false)
                        //   onFailToAnalysis(exception)
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
