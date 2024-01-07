package com.yapp.common.util

import androidx.compose.ui.Modifier

inline fun Modifier.runIf(
    condition: Boolean,
    block: Modifier.() -> Modifier,
) = if (condition) {
    block()
} else {
    this
}