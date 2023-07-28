package com.yapp.common.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalView

private const val KeyBoardVisibleThreshold = 0.15f

/**
 * 키보드의 Show/Hide 상태를 가져온다.
 * @return 키보드의 visible 상태
 */
@Composable
fun rememberKeyboardVisible(
    initialKeyboardState: Boolean = false,
): State<Boolean> {
    val view = LocalView.current
    return produceState(initialValue = initialKeyboardState) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            value = view.isKeyboardOpen()
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
        awaitDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }
}

private fun View.isKeyboardOpen(): Boolean {
    val rect = Rect().also {
        getWindowVisibleDisplayFrame(it)
    }
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom
    return keypadHeight > screenHeight * KeyBoardVisibleThreshold
}