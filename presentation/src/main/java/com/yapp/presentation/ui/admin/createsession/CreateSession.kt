package com.yapp.presentation.ui.admin.createsession

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.flow.collectAsStateWithLifecycle
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.util.rememberKeyboardVisible
import com.yapp.common.yds.YDSAppBar
import com.yapp.common.yds.YDSButtonLarge
import com.yapp.common.yds.YdsButtonState
import com.yapp.domain.model.types.NeedToAttendType
import com.yapp.presentation.R
import com.yapp.presentation.ui.admin.createsession.CreateSessionContract.CreateSessionUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateSession(
    viewModel: CreateSessionViewModel = hiltViewModel(),
    onBackButtonClicked: (() -> Unit)? = null,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CreateSessionScreen(
        uiState = uiState,
        onBackButtonClicked = { onBackButtonClicked?.invoke() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateSessionScreen(
    uiState: CreateSessionUiState,
    onBackButtonClicked: (() -> Unit),
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by rememberKeyboardVisible()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CreateSessionDateWriterDialog(
            onDismissRequest = { showDialog = false },
            onClickConfirm = { showDialog = false }
        )
    }

    ModalBottomSheetLayout(
        sheetContent = {
            CreateSessionTypeBottomSheet(
                sessionStates = NeedToAttendType.values(),
                onClickItem = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                }
            )
        },
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            topBar = {
                YDSAppBar(
                    modifier = Modifier
                        .background(AttendanceTheme.colors.backgroundColors.background)
                        .padding(horizontal = 4.dp),
                    title = stringResource(id = R.string.create_session),
                    onClickBackButton = { onBackButtonClicked.invoke() }
                )
            },
            backgroundColor = AttendanceTheme.colors.backgroundColors.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    CreateSessionTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        input = title,
                        onValueChange = { title = it },
                    )
                    CreateSessionSelector(
                        content = "",
                        placeHolderTitle = "세션 타입을 선택해주세요.",
                        onClickSelector = {
                            coroutineScope.launch {
                                if (isKeyboardVisible) {
                                    focusManager.clearFocus()
                                    delay(300)
                                }

                                sheetState.show()
                            }
                        }
                    )
                    CreateSessionSelector(
                        content = "",
                        placeHolderTitle = "날짜를 입력해주세요.",
                        onClickSelector = {
                            coroutineScope.launch {
                                if (isKeyboardVisible) {
                                    focusManager.clearFocus()
                                    delay(300)
                                }

                                showDialog = true
                            }
                        },
                    )
                    CreateSessionCode(code = uiState.code)
                    CreateSessionDescriptionTextField(
                        description = description,
                        onValueChange = { description = it },
                        maxLength = 100
                    )
                }
                YDSButtonLarge(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    text = "세션 생성하기",
                    state = YdsButtonState.ENABLED
                ) {
                    // TODO 세션 생성
                }
            }
        }
    }
}

@Composable
fun CreateSessionTypeBottomSheet(
    modifier: Modifier = Modifier,
    sessionStates: Array<NeedToAttendType>,
    onClickItem: (NeedToAttendType) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .navigationBarsWithImePadding()
            .background(color = AttendanceTheme.colors.backgroundColors.backgroundElevated)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        sessionStates.forEach { needToAttendType ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickItem(needToAttendType) }
                    .padding(vertical = 8.dp),
                text = needToAttendType.value,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CreateSessionTextField(
    modifier: Modifier = Modifier,
    input: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = input,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        modifier = modifier
            .background(
                color = AttendanceTheme.colors.grayScale.Gray200,
                shape = RoundedCornerShape(50.dp)
            ),
        placeholder = {
            Text(
                text = "세션명을 입력해주세요.",
                color = AttendanceTheme.colors.grayScale.Gray400,
                style = AttendanceTypography.body1,
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
fun CreateSessionSelector(
    modifier: Modifier = Modifier,
    content: String,
    placeHolderTitle: String? = null,
    onClickSelector: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = TextFieldDefaults.MinHeight)
            .clip(shape = RoundedCornerShape(50.dp))
            .background(color = AttendanceTheme.colors.grayScale.Gray200)
            .clickable { onClickSelector() },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = content.ifEmpty { placeHolderTitle ?: "" },
            color = if (content.isBlank()) AttendanceTheme.colors.grayScale.Gray400 else AttendanceTheme.colors.grayScale.Gray800,
            style = AttendanceTypography.body1,
        )
    }
}

@Composable
fun CreateSessionCode(
    modifier: Modifier = Modifier,
    code: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "코드 : $code",
            textAlign = TextAlign.End,
            color = AttendanceTheme.colors.grayScale.Gray800,
            style = AttendanceTypography.body1.copy(
                fontWeight = FontWeight.Bold
            ),
        )
    }
}

@Composable
fun CreateSessionDescriptionTextField(
    modifier: Modifier = Modifier,
    description: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 140.dp)
            .background(AttendanceTheme.colors.backgroundColors.background)
            .border(
                width = 1.dp,
                color = AttendanceTheme.colors.grayScale.Gray200,
                shape = RoundedCornerShape(8.dp)
            ),
        value = description,
        onValueChange = {
            if (it.length <= maxLength)
                onValueChange(it)
        },
        maxLines = 6,
        placeholder = {
            Text(
                text = "세션 설명을 입력해주세요.",
                color = AttendanceTheme.colors.grayScale.Gray400,
                style = AttendanceTypography.body1,
                textAlign = TextAlign.Start
            )
        },
        textStyle = AttendanceTypography.body2.copy(
            color = AttendanceTheme.colors.grayScale.Gray800
        ),
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
