package com.yapp.common.util

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ColumnScope.Spacer(space: Dp) {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = space))
}