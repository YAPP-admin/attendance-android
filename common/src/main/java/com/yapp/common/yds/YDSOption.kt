package com.yapp.common.yds

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun YDSOption(types: List<String>, selectedType: String?, onTypeClicked: (String) -> Unit) {
    val rowNum = 2
    Column {
        repeat(types.size / rowNum) { row ->
            Row {
                repeat(rowNum) { index ->
                    val type = types[rowNum * row + index]
                    YDSChoiceButton(
                        text = type,
                        modifier = Modifier.padding(end = 12.dp, bottom = 12.dp),
                        state = if (selectedType == type) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                        onClick = { onTypeClicked(type) }
                    )
                }
            }
        }
    }
}

interface YDSOptionState<T> {
    val items: List<T>
    val selectedOption: T?

    fun select(type: YDSOptionState<T>.() -> (YDSOptionState<T>)) : YDSOptionState<T> = type(this)
}
