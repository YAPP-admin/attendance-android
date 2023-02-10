package com.yapp.presentation.ui.member.signup.name

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.yapp.common.theme.*
import com.yapp.common.util.KeyboardVisibilityUtils
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R
import com.yapp.presentation.ui.member.signup.name.NameContract.NameSideEffect
import com.yapp.presentation.ui.member.signup.name.NameContract.NameUiEvent.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Name(
    viewModel: NameViewModel = hiltViewModel(),
    onClickBackBtn: () -> Unit,
    onClickNextBtn: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var isKeyboardOpened by remember { mutableStateOf(false) }
    val keyboardVisibilityUtils = KeyboardVisibilityUtils(
        window = (LocalContext.current as Activity).window,
        onShowKeyboard = { isKeyboardOpened = true },
        onHideKeyboard = { isKeyboardOpened = false }
    )

    val onBackButtonClick by remember { mutableStateOf({ viewModel.setEvent(OnBackButtonClick) }) }
    val onNextButtonClick: () -> Unit by remember {
        mutableStateOf({ viewModel.setEvent(OnNextButtonClick) })
    }
    val onInputNameChange: (String) -> Unit by remember {
        mutableStateOf({ viewModel.setEvent(InputName(it)) })
    }

    Scaffold(
        topBar = { YDSAppBar(onClickBackButton = { showDialog = true }) },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsWithImePadding()
    ) {
        if (showDialog) {
            YDSPopupDialog(
                title = stringResource(id = R.string.name_cancel_dialog_title),
                content = stringResource(id = R.string.name_cancel_dialog_subtitle),
                negativeButtonText = stringResource(id = R.string.name_cancel_dialog_no),
                positiveButtonText = stringResource(id = R.string.name_cancel_dialog_cancel),
                onClickPositiveButton = { onBackButtonClick() },
                onClickNegativeButton = { showDialog = false },
                onDismiss = { showDialog = false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .align(Alignment.TopCenter)
            ) {
                Title()
                InputName(
                    name = uiState.name,
                    onInputName = { onInputNameChange(it) }
                )
            }

            NextButton(
                enabled = uiState.name.isNotBlank(),
                isKeyboardOpened = isKeyboardOpened,
                modifier = Modifier.align(Alignment.BottomCenter),
                onClickNextBtn = { onNextButtonClick() },
                keyboardVisibilityUtils = keyboardVisibilityUtils
            )
        }
    }

    BackHandler {
        showDialog = true
    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NameSideEffect.NavigateToPreviousScreen -> {
                    onClickBackBtn()
                }

                is NameSideEffect.NavigateToNextScreen -> {
                    onClickNextBtn(effect.name)
                }
            }
        }
    }
}

@Composable
fun Title() {
    Column {
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
    TextField(
        value = name,
        onValueChange = { onInputName(it); },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray_200, shape = RoundedCornerShape(50.dp)),
        placeholder = {
            Text(
                text = stringResource(id = R.string.name_example_hint),
                color = Gray_400,
                style = AttendanceTypography.body1
            )
        },
        textStyle = AttendanceTypography.body2.copy(color = Gray_800),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun NextButton(
    enabled: Boolean,
    isKeyboardOpened: Boolean,
    modifier: Modifier,
    onClickNextBtn: () -> Unit,
    keyboardVisibilityUtils: KeyboardVisibilityUtils
) {
    Box(
        modifier = modifier,
    ) {
        if (isKeyboardOpened) {
            OnKeyboardNextButton(
                enabled = enabled,
                onClickNextBtn = onClickNextBtn,
                keyboardVisibilityUtils = keyboardVisibilityUtils
            )
        } else {
            YDSButtonLarge(
                text = stringResource(id = R.string.name_next_button),
                state = if (enabled) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = {
                    if (enabled) {
                        onClickNextBtn()
                        keyboardVisibilityUtils.detachKeyboardListener()
                    }
                },
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
            )
        }
    }
}


@Composable
fun OnKeyboardNextButton(
    enabled: Boolean,
    onClickNextBtn: () -> Unit,
    keyboardVisibilityUtils: KeyboardVisibilityUtils
) {
    Button(
        enabled = enabled,
        contentPadding = PaddingValues(0.dp),
        onClick = {
            onClickNextBtn()
            keyboardVisibilityUtils.detachKeyboardListener()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Yapp_Orange,
            disabledBackgroundColor = Gray_400
        ),
        elevation = null
    ) {
        Text(
            style = AttendanceTypography.h3,
            text = stringResource(id = R.string.name_next_button),
            color = Color.White
        )
    }
}
