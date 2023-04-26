package com.yapp.common.util

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.Window

class KeyboardVisibilityUtils(
    private val window: Window,
    private val onShowKeyboard: () -> Unit,
    private val onHideKeyboard: () -> Unit
) {
    private val windowVisibleDisplayFrame = Rect()
    private var lastVisibleDecorViewHeight: Int = Int.MAX_VALUE
    private val MIN_KEYBOARD_HEIGHT_PX = 100

    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        window.decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
        val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

        if (lastVisibleDecorViewHeight != 0) {
            if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                onShowKeyboard.invoke()
            } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                onHideKeyboard.invoke()
            }
        }

        lastVisibleDecorViewHeight = visibleDecorViewHeight
    }

    init {
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun detachKeyboardListener() {
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

}