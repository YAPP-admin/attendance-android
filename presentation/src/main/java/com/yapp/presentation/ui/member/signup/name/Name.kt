package com.yapp.presentation.ui.member.signup.name

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.util.rememberKeyboardVisible
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YDSPopupDialog
import com.yapp.common.yds.YdsButtonState
import com.yapp.presentation.R
import com.yapp.presentation.ui.member.signup.name.NameContract.NameSideEffect
import com.yapp.presentation.ui.member.signup.name.NameContract.NameUiEvent.InputName
import com.yapp.presentation.ui.member.signup.name.NameContract.NameUiEvent.OnBackButtonClick
import com.yapp.presentation.ui.member.signup.name.NameContract.NameUiEvent.OnCancelButtonClick
import com.yapp.presentation.ui.member.signup.name.NameContract.NameUiEvent.OnDismissDialogButtonClick
import com.yapp.presentation.ui.member.signup.name.NameContract.NameUiEvent.OnNextButtonClick
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Name(
    viewModel: NameViewModel = hiltViewModel(),
    onClickBackBtn: () -> Unit,
    onClickNextBtn: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isKeyboardVisible by rememberKeyboardVisible()

    val onCancelButtonClick by remember { mutableStateOf({ viewModel.setEvent(OnCancelButtonClick) }) }
    val onNextButtonClick: () -> Unit by remember {
        mutableStateOf({ viewModel.setEvent(OnNextButtonClick) })
    }
    val onInputNameChange: (String) -> Unit by remember {
        mutableStateOf({ viewModel.setEvent(InputName(it)) })
    }
    val onBackButtonClick: () -> Unit by remember {
        mutableStateOf({ viewModel.setEvent(OnBackButtonClick) })
    }
    val onDismissDialogButtonClick: () -> Unit by remember {
        mutableStateOf({ viewModel.setEvent(OnDismissDialogButtonClick) })
    }

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                onClickBackButton = { onBackButtonClick() }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsWithImePadding()
    ) { contentPadding ->
        if (uiState.showDialog) {
            YDSPopupDialog(
                title = stringResource(id = R.string.name_cancel_dialog_title),
                content = stringResource(id = R.string.name_cancel_dialog_subtitle),
                negativeButtonText = stringResource(id = R.string.name_cancel_dialog_no),
                positiveButtonText = stringResource(id = R.string.name_cancel_dialog_cancel),
                onClickPositiveButton = { onCancelButtonClick() },
                onClickNegativeButton = { onDismissDialogButtonClick() },
                onDismiss = { onDismissDialogButtonClick() }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AttendanceTheme.colors.backgroundColors.background)
                .padding(contentPadding)
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
                isKeyboardVisible = isKeyboardVisible,
                modifier = Modifier.align(Alignment.BottomCenter),
                onClickNextBtn = { onNextButtonClick() },
            )
        }
    }

    BackHandler {
        onBackButtonClick()
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
            color = AttendanceTheme.colors.grayScale.Gray1200,
            style = AttendanceTypography.h1
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.name_subtitle),
            color = AttendanceTheme.colors.grayScale.Gray800,
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
            .background(
                color = AttendanceTheme.colors.grayScale.Gray200,
                shape = RoundedCornerShape(50.dp)
            ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.name_example_hint),
                color = AttendanceTheme.colors.grayScale.Gray400,
                style = AttendanceTypography.body1
            )
        },
        textStyle = AttendanceTypography.body2.copy(color = AttendanceTheme.colors.grayScale.Gray800),
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
    isKeyboardVisible: Boolean,
    modifier: Modifier,
    onClickNextBtn: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        if (isKeyboardVisible) {
            OnKeyboardNextButton(
                enabled = enabled,
                onClickNextBtn = onClickNextBtn,
            )
        } else {
            YDSButtonLarge(
                text = stringResource(id = R.string.name_next_button),
                state = if (enabled) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = {
                    if (enabled) {
                        onClickNextBtn()
                    }
                },
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
            )
        }
    }
}


@Composable
fun OnKeyboardNextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClickNextBtn: () -> Unit,
) {
    Button(
        enabled = enabled,
        contentPadding = PaddingValues(0.dp),
        onClick = {
            onClickNextBtn()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AttendanceTheme.colors.mainColors.YappOrange,
            disabledBackgroundColor = AttendanceTheme.colors.grayScale.Gray400
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
