package com.yapp.presentation.ui.member.signup

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.common.theme.*
import com.yapp.common.theme.Gray_800
import com.yapp.common.util.KeyboardVisibilityUtils
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R


@Composable
fun Name(
    viewModel: NameViewModel = hiltViewModel(),
    onClickBackBtn: () -> Unit,
    onClickNextBtn: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var isKeyboardOpened by remember { mutableStateOf(false) }
    val keyboardVisibilityUtils = KeyboardVisibilityUtils(
        window = (LocalContext.current as Activity).window,
        onShowKeyboard = { isKeyboardOpened = true },
        onHideKeyboard = { isKeyboardOpened = false })

    Scaffold(
        topBar = { YDSAppBar(onClickBackButton = { showDialog = !showDialog }) },
        modifier = Modifier.fillMaxSize()
    ) {
        if (showDialog) {
            YDSPopupDialog(
                title = stringResource(id = R.string.name_cancel_dialog_title),
                content = stringResource(id = R.string.name_cancel_dialog_subtitle),
                negativeButtonText = stringResource(id = R.string.name_cancel_dialog_no),
                positiveButtonText = stringResource(id = R.string.name_cancel_dialog_cancel),
                onClickPositiveButton = { onClickBackBtn() },
                onClickNegativeButton = { showDialog = !showDialog },
                onDismiss = { showDialog = !showDialog }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Title()
                InputName(
                    name = uiState.name,
                    onInputName = { viewModel.setEvent(NameContract.NameUiEvent.InputName(it)) }
                )
            }
            NextButton(
                name = uiState.name,
                isKeyboardOpened = isKeyboardOpened,
                modifier = Modifier.align(Alignment.BottomCenter),
                onClickNextBtn = onClickNextBtn
            )
        }
    }
}

@Composable
fun Title() {
    Column() {

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.name_title),
            color = Color.Black,
            style = AttendanceTypography.h1
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.name_subtitle),
            color = Gray_800,
            style = AttendanceTypography.body1
        )
        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun InputName(name: String, onInputName: (String) -> Unit) {

    BasicTextField(
        value = name,
        onValueChange = { onInputName(it); },
        singleLine = true,
        textStyle = AttendanceTypography.body2.copy(color = Gray_800),
        cursorBrush = SolidColor(Yapp_Orange),
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Gray_200, shape = RoundedCornerShape(50.dp))
            ) {
                Box(modifier = Modifier.padding(20.dp)) {
                    if (name.isBlank()) {
                        Text(
                            text = stringResource(id = R.string.name_example_hint),
                            color = Gray_400,
                            style = AttendanceTypography.body1
                        )
                    } else {
                        innerTextField()
                    }
                }
            }
        }
    )
}

@Composable
fun NextButton(
    name: String,
    isKeyboardOpened: Boolean,
    modifier: Modifier,
    onClickNextBtn: () -> Unit
) {
    Column(modifier = modifier) {
        if (isKeyboardOpened) {
            OnKeyboardNextButton(
                state = if (name.isEmpty()) YdsButtonState.DISABLED else YdsButtonState.ENABLED,
                onClick = { onClickNextBtn() })
        } else {
            YDSButtonLarge(
                text = stringResource(id = R.string.name_next_button),
                state = if (name.isEmpty()) YdsButtonState.DISABLED else YdsButtonState.ENABLED,
                onClick = { onClickNextBtn() },
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun OnKeyboardNextButton(
    state: YdsButtonState,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick, modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = when (state) {
            YdsButtonState.DISABLED -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Gray_400
                )
            }
            YdsButtonState.ENABLED,
            YdsButtonState.PRESSED
            -> {
                ButtonDefaults.buttonColors(
                    backgroundColor = Yapp_Orange
                )
            }
        },
        elevation = null
    ) {
        Text(
            text = stringResource(id = R.string.name_next_button),
            color = Color.White
        )
    }

}
