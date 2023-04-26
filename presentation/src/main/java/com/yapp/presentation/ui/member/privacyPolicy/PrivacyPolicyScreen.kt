package com.yapp.presentation.ui.member.privacyPolicy

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.systemBarsPadding
import com.yapp.common.theme.AttendanceTheme
import com.yapp.common.yds.YDSAppBar


@Composable
fun PrivacyPolicyScreen(
    onClickBackButton: () -> Unit
) {
    val url = remember { "https://yapprecruit.notion.site/8b561d1b0fa449bba4db395f53a559f3" }

    Scaffold(
        topBar = {
            YDSAppBar(
                modifier = Modifier.background(AttendanceTheme.colors.backgroundColors.background),
                onClickBackButton = { onClickBackButton() },
                title = "개인정보 처리방침"
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            WebViewComposable(modifier = Modifier.fillMaxSize(), url = url)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComposable(
    modifier: Modifier = Modifier,
    url: String
) {
    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                settings.builtInZoomControls = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.javaScriptEnabled = true

                scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_INSET

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        return false
                    }
                }
                loadUrl(url)
            }
        },
        update = { it.loadUrl(url) }
    )
}