package com.app.sample.compose.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.app.base.compose.dialog.alert.AlertDialog
import com.app.base.compose.dialog.alert.AlertDialogDefaults
import com.app.base.compose.dialog.alert.AlertDialogState
import com.app.base.compose.dialog.alert.rememberAlertDialogState
import com.app.base.compose.dialog.loading.LoadingDialog
import com.app.base.compose.dialog.loading.rememberLoadingDialogState
import com.app.sample.compose.widget.PageButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DialogsScreen() {
    val scope = rememberCoroutineScope()

    val loadingDialogState = rememberLoadingDialogState()
    val alertDialogState1 = rememberAlertDialogState()
    val alertDialogState2 = rememberAlertDialogState()

    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        PageButton("Loading Dialog") {
            loadingDialogState.show()
            scope.launch {
                delay(2000)
                loadingDialogState.hide()
            }
        }

        PageButton("Alert Dialog") {
            alertDialogState1.show()
        }

        PageButton("Custom Alert Dialog") {
            alertDialogState2.show()
        }
    }

    LoadingDialog(state = loadingDialogState)
    DemoAlertDialog(state = alertDialogState1)
    DemoCustomAlertDialog(state = alertDialogState2)
}

@Composable
private fun DemoAlertDialog(state: AlertDialogState) {
    AlertDialog(
        state = state,
        title = "I am title",
        message = """
            在设计系统中，primary、secondary 和 tertiary 颜色用于区分不同类型的 UI 元素和操作。以下是它们的主要区别和用途：

            Primary
            主要用途: 用于强调应用的主要品牌色，代表最重要的元素和操作。
            常见使用: 顶部栏、主要按钮、链接等。
            
            Secondary
            主要用途: 辅助色，用于支持和补充主色，提供更多的视觉层次。
            常见使用: 次要按钮、选项卡、强调特定信息等。
            
            Tertiary
            主要用途: 提供额外的色彩选择，用于进一步区分信息或操作。
            常见使用: 特殊的背景、通知标签、图标等。
            
            选择和使用
            对比和调和: 确保三者之间有足够的对比，以便用户能轻松区分不同的功能。
            一致性: 保持颜色一致，支持品牌视觉识别。
            灵活性: 在应用中灵活使用，适应不同的设计需求和用户界面。
            通过合理使用这些颜色，可以增强应用的视觉效果和用户体验。
        """.trimIndent(),
        messageStyle = AlertDialogDefaults.DefaultMessageStyle.copy(textAlign = TextAlign.Start, fontSize = 12.sp),
        positiveButton = "OK",
        onPositiveClick = {
            state.hide()
        },
        negativeButton = "Cancel",
        onNegativeClick = {
            state.hide()
        },
    )
}

@Composable
private fun DemoCustomAlertDialog(state: AlertDialogState) {
    AlertDialog(
        state = state,
        title = {
            Text("I am title")
        },
        content = {
            Text(
                text = """
                    High level element that displays text and provides semantics / accessibility information.
                    The default style uses the LocalTextStyle provided by the MaterialTheme / components. If you are setting your own style, you may want to consider first retrieving LocalTextStyle, and using TextStyle. copy to keep any theme defined attributes, only modifying the specific attributes you want to override.
                """.trimIndent(),
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        },
        positiveButton = {
            TextButton(onClick = {
                state.hide()
            }, Modifier.fillMaxWidth()) {
                Text("OK")
            }
        },
        negativeButton = {
            TextButton(onClick = {
                state.hide()
            }, Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }
    )
}
