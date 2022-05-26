package com.yapp.presentation.ui.member.qrcodescanner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.style.TextAlign
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
import com.yapp.common.yds.YDSProgressBar
import com.yapp.common.yds.YDSToast
import com.yapp.presentation.R
import com.yapp.presentation.util.permission.PermissionManager
import com.yapp.presentation.util.permission.PermissionState
import com.yapp.presentation.util.permission.PermissionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.util.concurrent.Executors

@Composable
fun QrCodeScanner(
    viewModel: QrCodeViewModel = hiltViewModel(),
    navigateToPreviousScreen: () -> Unit,
) {
    val context = LocalContext.current
    var showQrScanner by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var toastVisible by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is QrCodeContract.QrCodeUiSideEffect.ShowToast -> {
                    toastVisible = true
                }
                is QrCodeContract.QrCodeUiSideEffect.ShowToastAndHide -> {
                    toastVisible = true
                    delay(1000L)
                    toastVisible = false
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

    if (uiState.isLoading) {
        YDSProgressBar()
    } else {
        if (showQrScanner) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CameraPreview(
                    afterDetectedCode = { qrCode ->
                        viewModel.setEvent(QrCodeContract.QrCodeUiEvent.ScanQrCode(qrCode.rawValue))
                    },
                    detectedError = { exception ->
                        viewModel.setEvent(QrCodeContract.QrCodeUiEvent.GetScannerError(exception))
                    }
                )
                ScannerDecoration(
                    modifier = Modifier,
                    navigateToPreviousScreen = navigateToPreviousScreen
                )
            }

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val (noticeText, checkIcon) = createRefs()
                val guideline = createGuidelineFromTop(fraction = 0.5f)

                when (uiState.attendanceState) {
                    QrCodeContract.AttendanceState.STAND_BY -> {
                        if (uiState.maginotlineTime.isBlank()) {
                            ErrorText(
                                modifier = Modifier.constrainAs(noticeText) {
                                    bottom.linkTo(guideline, 186.dp)
                                    absoluteLeft.linkTo(parent.absoluteLeft)
                                    absoluteRight.linkTo(parent.absoluteRight)
                                }
                            )
                        } else {
                            NoticeText(
                                modifier = Modifier.constrainAs(noticeText) {
                                    bottom.linkTo(guideline, 162.dp)
                                    absoluteLeft.linkTo(parent.absoluteLeft)
                                    absoluteRight.linkTo(parent.absoluteRight)
                                },
                                maginotlineTime = uiState.maginotlineTime
                            )
                        }
                    }
                    QrCodeContract.AttendanceState.SUCCESS -> {
                        SuccessLottie(
                            modifier = Modifier.constrainAs(checkIcon) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                absoluteLeft.linkTo(parent.absoluteLeft)
                                absoluteRight.linkTo(parent.absoluteRight)
                            },
                            navigateToPreviousScreen = { navigateToPreviousScreen() }
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = toastVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 110.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            YDSToast(
                text = uiState.toastMessage
            )
        }
    }

    if (showDialog) {
        YDSPopupDialog(
            title = stringResource(id = R.string.member_qr_permission_dialog_title_text),
            content = stringResource(id = R.string.member_qr_permission_dialog_content_text),
            negativeButtonText = stringResource(id = R.string.member_qr_permission_dialog_negative_button),
            positiveButtonText = stringResource(id = R.string.member_qr_permission_dialog_positive_button),
            onClickPositiveButton = { intentToAppSetting(context) },
            onClickNegativeButton = { navigateToPreviousScreen() },
            onDismiss = { navigateToPreviousScreen() }
        )
    }
}

@Composable
fun CameraPreview(
    afterDetectedCode: (code: Barcode) -> Unit,
    detectedError: (Exception) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Surface {
        AndroidView(
            factory = { androidViewContext ->
                val mainExecutor = ContextCompat.getMainExecutor(androidViewContext)
                val cameraExecutor = Executors.newSingleThreadExecutor()
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                val previewView = PreviewView(androidViewContext).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val barcodeAnalyzer =
                        QrCodeAnalyzer(
                            onQrCodeDetected = { code ->
                                afterDetectedCode(code)
                            },
                            onFailToAnalysis = { exception ->
                                detectedError(exception)
                            }
                        )

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(cameraExecutor, barcodeAnalyzer)
                        }
                    
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                }, mainExecutor)
                previewView
            },
            modifier = Modifier.fillMaxSize(),
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
fun ErrorText(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.member_qr_fail_to_get_information_text),
        color = Color.White,
        style = AttendanceTypography.body1,
        textAlign = TextAlign.Center
    )
}

@Composable
fun NoticeText(
    modifier: Modifier = Modifier,
    maginotlineTime: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = maginotlineTime + stringResource(id = R.string.member_qr_time_inform_text),
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