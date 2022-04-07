package com.yapp.presentation.ui.member.qrcodescanner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
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
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.mlkit.vision.barcode.common.Barcode
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YDSToast
import com.yapp.presentation.R
import com.yapp.presentation.util.permission.PermissionManager
import com.yapp.presentation.util.permission.PermissionState
import com.yapp.presentation.util.permission.PermissionType
import kotlinx.coroutines.flow.collect
import java.util.concurrent.Executors

@Composable
fun QrCodeScanner(
    viewModel: QrCodeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navigateToPreviousScreen: () -> Unit,
) {
    val context = LocalContext.current
    var showQrScanner by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is QrCodeContract.QrCodeUiSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    PermissionManager.requestPermission(
        context,
        PermissionType.CAMERA
    ) { permissionState ->
        when (permissionState) {
            PermissionState.GRANTED -> {
                showQrScanner = true
                showDialog = false
            }
            PermissionState.NEED_DESCRIPTION -> {
                showQrScanner = false
                showDialog = true
            }
            PermissionState.DENIED -> {
                showQrScanner = false
                showDialog = true
            }
        }
    }

    if (showQrScanner) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            CameraPreview { qrCode ->
                viewModel.setEvent(QrCodeContract.QrCodeUiEvent.ScanQrCode(qrCode.rawValue))
            }
            ScannerDecoration(
                modifier = modifier,
                navigateToPreviousScreen = navigateToPreviousScreen
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            val (noticeText, checkIcon, completeNoticeBox) = createRefs()
            val guideline = createGuidelineFromTop(fraction = 0.5f)

            when (uiState.attendanceState) {
                QrCodeContract.AttendanceState.STAND_BY -> {
                    NoticeText(
                        modifier.constrainAs(noticeText) {
                            bottom.linkTo(guideline, 162.dp)
                            absoluteLeft.linkTo(parent.absoluteLeft)
                            absoluteRight.linkTo(parent.absoluteRight)
                        }
                    )
                }
                QrCodeContract.AttendanceState.SUCCESS -> {
                    SuccessLottie(
                        modifier = modifier.constrainAs(checkIcon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            absoluteLeft.linkTo(parent.absoluteLeft)
                            absoluteRight.linkTo(parent.absoluteRight)
                        },
                        navigateToPreviousScreen = { navigateToPreviousScreen() }
                    )
                }
                QrCodeContract.AttendanceState.COMPLETE -> {
                    YDSToast(
                        modifier = Modifier
                            .constrainAs(completeNoticeBox) {
                                bottom.linkTo(guideline, 162.dp)
                                absoluteLeft.linkTo(parent.absoluteLeft)
                                absoluteRight.linkTo(parent.absoluteRight)
                            },
                        text = stringResource(id = R.string.member_qr_complete_inform_text)
                    )
                }
            }
        }
    }

    if (showDialog) {
        YDSPopupDialog(
            title = stringResource(id = R.string.member_qr_permission_dialog_title_text),
            content = stringResource(id = R.string.member_qr_permission_dialog_content_text),
            negativeButtonText = stringResource(id = R.string.member_qr_permission_dialog_negative_text),
            positiveButtonText = stringResource(id = R.string.member_qr_permission_dialog_positive_text),
            onClickPositiveButton = { intentToAppSetting(context) },
            onClickNegativeButton = { navigateToPreviousScreen() },
            onDismiss = { navigateToPreviousScreen() }
        )
    }
}

@Composable
fun CameraPreview(
    afterDetectedCode: (code: Barcode) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }

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
                            onQrCodeDetected = { code ->
                                afterDetectedCode(code)
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
    navigateToPreviousScreen: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        IconButton(
            modifier = modifier
                .align(Alignment.TopEnd)
                .padding(top = 14.dp, end = 14.dp),
            onClick = navigateToPreviousScreen
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_close),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
        Icon(
            modifier = modifier.align(Alignment.Center),
            painter = painterResource(id = R.drawable.icon_qr_area),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}

@Composable
fun NoticeText(modifier: Modifier = Modifier) {
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

@Composable
fun SuccessLottie(
    modifier: Modifier = Modifier,
    navigateToPreviousScreen: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.qr_check_success))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1
    )

    LaunchedEffect(progress) {
        if (progress == 1f) {
            navigateToPreviousScreen()
        }
    }
    LottieAnimation(
        composition,
        progress,
        modifier = modifier
    )
}

private fun intentToAppSetting(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:" + context.packageName)
    )
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(context, intent, null)
}