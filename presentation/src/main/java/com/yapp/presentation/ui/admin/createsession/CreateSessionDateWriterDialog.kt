package com.yapp.presentation.ui.admin.createsession

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.theme.AttendanceTypography
import com.yapp.common.yds.YDSButtonSmall
import com.yapp.common.yds.YdsButtonState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateSessionDateWriterDialog(
    modifier: Modifier = Modifier,
    date: LocalDateTime? = null,
    onDismissRequest: () -> Unit,
    onClickConfirm: (String) -> Unit,
) {
    var state by remember {
        mutableStateOf(
            CreateSessionDateWriterDialogState(
                year = date?.year?.toString() ?: "",
                month = date?.month?.toString() ?: "",
                day = date?.dayOfMonth?.toString() ?: "",
                hour = date?.hour?.toString() ?: "",
                minute = date?.minute?.toString() ?: ""
            )
        )
    }

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        // 다이얼로그 내부에 위치해야만 다이얼로그의 포커스 추적 가능
        val focusManager = LocalFocusManager.current
        val yearFocusRequester = FocusRequester()
        val monthFocusRequester = FocusRequester()
        val dayFocusRequester = FocusRequester()
        val hourFocusRequester = FocusRequester()
        val minuteFocusRequester = FocusRequester()

        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = AttendanceTheme.colors.backgroundColors.backgroundElevated)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CreateSessionDateTextField(
                    modifier = Modifier.width(80.dp),
                    input = state.year,
                    focusRequester = yearFocusRequester,
                    nextFocusRequester = monthFocusRequester,
                    isError = state.isValidYear.not(),
                    maxLength = 4,
                    onValueChange = { state = state.copy(year = it) }
                )
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                CreateSessionDateText(text = "년")
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                CreateSessionDateTextField(
                    modifier = Modifier.width(52.dp),
                    input = state.month,
                    focusRequester = monthFocusRequester,
                    nextFocusRequester = dayFocusRequester,
                    isError = state.isValidMonth.not(),
                    maxLength = 2,
                    onValueChange = { state = state.copy(month = it) }
                )
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                CreateSessionDateText(text = "월")
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                CreateSessionDateTextField(
                    modifier = Modifier.width(52.dp),
                    input = state.day,
                    focusRequester = dayFocusRequester,
                    nextFocusRequester = hourFocusRequester,
                    isError = state.isValidDay.not(),
                    maxLength = 2,
                    onValueChange = { state = state.copy(day = it) }
                )
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                CreateSessionDateText(text = "일")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CreateSessionDateTextField(
                    modifier = Modifier.width(52.dp),
                    input = state.hour,
                    focusRequester = hourFocusRequester,
                    nextFocusRequester = minuteFocusRequester,
                    isError = state.isValidHour.not(),
                    maxLength = 2,
                    onValueChange = { state = state.copy(hour = it) }
                )
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                CreateSessionDateText(text = "시")
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                CreateSessionDateTextField(
                    modifier = Modifier.width(52.dp),
                    input = state.minute,
                    focusRequester = minuteFocusRequester,
                    isError = state.isValidMinute.not(),
                    imeAction = ImeAction.Done,
                    maxLength = 2,
                    onValueChange = {
                        if (it.length == 2)
                            focusManager.clearFocus()

                        state = state.copy(minute = it)
                    },
                    onKeyboardAction = {
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.padding(horizontal = 1.dp))
                CreateSessionDateText(text = "분")
            }
            YDSButtonSmall(
                text = "확인",
                state =
                if (state.isValidDate) YdsButtonState.ENABLED
                else YdsButtonState.DISABLED,
                onClick = {
                    val dateTime = LocalDateTime.of(
                        state.year.toInt(),
                        state.month.toInt(),
                        state.day.toInt(),
                        state.hour.toInt(),
                        state.minute.toInt()
                    )
                    onClickConfirm(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                }
            )
        }
    }
}

@Composable
fun CreateSessionDateTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester? = null,
    isError: Boolean,
    input: String,
    maxLength: Int = Int.MAX_VALUE,
    imeAction: ImeAction = ImeAction.Next,
    onValueChange: (String) -> Unit,
    onKeyboardAction: () -> Unit = {
        nextFocusRequester?.requestFocus()
    },
) {
    TextField(
        modifier = modifier
            .background(
                color =
                if (isError.not()) AttendanceTheme.colors.grayScale.Gray200
                else AttendanceTheme.colors.etcColors.EtcRed,
                shape = RoundedCornerShape(8.dp)
            )
            .focusRequester(focusRequester),
        value = input,
        onValueChange = {
            if (it.length == maxLength)
                nextFocusRequester?.requestFocus()

            if (it.length <= maxLength)
                onValueChange(it)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions {
            onKeyboardAction()
        },
        textStyle = AttendanceTypography.body2.copy(
            color = if (isError.not()) AttendanceTheme.colors.grayScale.Gray800 else AttendanceTheme.colors.grayScale.Gray200
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            cursorColor = if (isError.not()) AttendanceTheme.colors.grayScale.Gray800 else AttendanceTheme.colors.grayScale.Gray200,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CreateSessionDateText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        color = AttendanceTheme.colors.grayScale.Gray800,
        style = AttendanceTypography.body1
    )
}
