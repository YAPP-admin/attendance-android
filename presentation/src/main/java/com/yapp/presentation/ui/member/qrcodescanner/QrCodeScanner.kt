package com.yapp.presentation.ui.member.qrcodescanner

import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.AttendanceTypography
import com.yapp.presentation.R
import com.yapp.presentation.util.permission.PermissionManager
import com.yapp.presentation.util.permission.PermissionState
import com.yapp.presentation.util.permission.PermissionType
import java.util.concurrent.Executors

@Composable
fun QrCodeScanner(
    viewModel: QrCodeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    moveBackToPreviousScreen: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        PermissionManager.requestPermission(
            context as AppCompatActivity,
            PermissionType.CAMERA
        ) { permissionState ->
            when (permissionState) {
                PermissionState.GRANTED -> {}
                PermissionState.NEED_DESCRIPTION -> {}
                PermissionState.DENIED -> {}
            }
        }
    }
}

@Composable
fun Scanner(
    modifier: Modifier = Modifier,
    moveBackToPreviousScreen: () -> Unit,
) {
    Surface {
        CameraPreview()
        ScannerDecoration(
            modifier = modifier,
            moveBackToPreviousScreen = moveBackToPreviousScreen
        )
    }
}

@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var code by remember { mutableStateOf("") }

    Surface {
        AndroidView(
            factory = { androidViewContext ->
                PreviewView(androidViewContext).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val cameraExecutor = Executors.newSingleThreadExecutor()
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    preview = Preview.Builder().build().apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraProvider = cameraProviderFuture.get()
                    val barcodeAnalyzer =
                        QrCodeAnalyzer(
                            onQrCodeDetected = { qrCodes ->
                                qrCodes.forEach { qrCode ->
                                    qrCode.rawValue?.let { qrCodeValue ->
                                        // todo: qrCode.cornerPoints 범위 내에서 인식된 것인지 확인
                                        // todo: code 확인해서 DB 반영
                                        code = qrCodeValue
                                        Toast.makeText(context, code, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onFailToAnalysis = { exception ->
                                Toast.makeText(
                                    context,
                                    "문제가 발생했습니다 코드를 다시 스캔해 주세요\n$exception",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(cameraExecutor, barcodeAnalyzer)
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (exception: Exception) {
                        Toast.makeText(context, "문제가 발생했습니다\n$exception", Toast.LENGTH_SHORT).show()
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )
    }
}

@Composable
fun ScannerDecoration(
    modifier: Modifier = Modifier,
    moveBackToPreviousScreen: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (informText, closeIcon, qrAreaIcon) = createRefs()
        InformText(
            modifier.constrainAs(informText) {
                bottom.linkTo(qrAreaIcon.top, 40.dp)
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
            }
        )
        IconButton(
            modifier = modifier
                .constrainAs(closeIcon) {
                    top.linkTo(parent.top, 14.dp)
                    absoluteRight.linkTo(parent.absoluteRight, 14.dp)
                },
            onClick = moveBackToPreviousScreen
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_close),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
        Icon(
            modifier = modifier.constrainAs(qrAreaIcon) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                absoluteRight.linkTo(parent.absoluteRight)
                absoluteLeft.linkTo(parent.absoluteLeft)
            },
            painter = painterResource(id = R.drawable.icon_qr_area),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}

@Composable
fun InformText(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.member_qr_time_inform_text),
            color = Color.White,
            style = AttendanceTypography.body1,
        )

        Text(
            text = stringResource(id = R.string.member_qr_late_inform_text),
            color = Color.White,
            style = AttendanceTypography.body1
        )
    }
}