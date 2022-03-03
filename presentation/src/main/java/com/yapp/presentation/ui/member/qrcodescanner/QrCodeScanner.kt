package com.yapp.presentation.ui.member.qrcodescanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.navigation.NavController
import com.google.common.util.concurrent.ListenableFuture
import com.yapp.common.theme.AttendanceTypography
import com.yapp.presentation.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun QrCodeScanner(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCamPermission = isGranted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        if (hasCamPermission) {
            Surface {
                CameraPreview()
                ScannerDecoration(
                    modifier = modifier,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var code = remember { mutableStateOf("") }

    Surface {
        AndroidView(
            factory = { androidViewContext ->
                PreviewView(androidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            update = { previewView ->
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                    ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    preview = androidx.camera.core.Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    val barcodeAnalyzer =
                        QrCodeAnalyzer { qrCodes ->
                            qrCodes.forEach { qrCode ->
                                qrCode.rawValue?.let { qrCodeValue ->
                                    Log.d("QrCodeAnalyzer", "QrCodeAnalyzer Result: $qrCodeValue")
                                    //Log.d("QrCodeAnalyzer", "Points: ${qrCode.cornerPoints}")
                                    code.value = qrCodeValue
                                    Toast.makeText(context, qrCodeValue, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, barcodeAnalyzer)
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.d("QrCodeAnalyzer", "CameraPreview: ${e.printStackTrace()}")
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )
    }
}

@Composable
fun ScannerDecoration(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
            onClick = {
                navController.popBackStack()
            }
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