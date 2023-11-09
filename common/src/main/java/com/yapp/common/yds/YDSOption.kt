package com.yapp.common.yds

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun YDSOption(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    types: List<String>,
    selectedType: String?,
    onTypeClicked: (String) -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
    ) {
        repeat(types.size) { index ->
            val type = types[index]
            YDSChoiceButton(
                text = type,
                modifier = Modifier.padding(bottom = 12.dp),
                state = if (selectedType == type) YdsButtonState.ENABLED else YdsButtonState.DISABLED,
                onClick = { onTypeClicked(type) }
            )
        }
    }
}

interface YDSOptionState<T> {
    val items: List<T>
    val selectedOption: T?
}
