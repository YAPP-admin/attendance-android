package com.yapp.presentation.ui.member.qrcodescanner

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class QrCodeAnalyzer @Inject constructor(
    private val onQrCodeDetected: (Barcode) -> Unit,
    private val onFailToAnalysis: (Exception) -> Unit
) : ImageAnalysis.Analyzer {
    companion object {
        const val SCAN_DURATION = 1L
    }

    private var lastAnalyzedTimeStamp = 0L

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val currentTimeStamp = System.currentTimeMillis()
        if (currentTimeStamp - lastAnalyzedTimeStamp >= TimeUnit.SECONDS.toMillis(SCAN_DURATION)) {
            imageProxy.setCropRect(Rect(0, 0, 200, 200))
            imageProxy.image?.let { imageToAnalyze ->
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()
                val qrCodeScanner = BarcodeScanning.getClient(options)
                val imageToProcess =
                    InputImage.fromMediaImage(imageToAnalyze, imageProxy.imageInfo.rotationDegrees)
                val availableAreaEdge = imageProxy.height / 2
                val availableArea = Rect(
                    (imageProxy.height - availableAreaEdge) / 2,
                    (imageProxy.width - availableAreaEdge) / 2,
                    (imageProxy.height + availableAreaEdge) / 2,
                    (imageProxy.width + availableAreaEdge) / 2
                ) // 대략 화면 높이의 절반 길이의 정사각형 안으로 들어와야 스캔되도록 구현함
                // todo: 정확히 스캔 범위 내로 들어와야 스캔되도록 수정

                qrCodeScanner.process(imageToProcess)
                    .addOnSuccessListener { qrCodes ->
                        if (qrCodes.size == 1) {
                            val detectedCode = qrCodes[0]
                            if (availableArea.contains(detectedCode.boundingBox!!)) {
                                onQrCodeDetected(detectedCode)
                            }
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
