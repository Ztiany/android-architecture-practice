package com.app.base.compose.dialog.alert

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.app.base.compose.design.component.divider.HorizontalDivider
import com.app.base.compose.design.component.divider.VerticalDivider
import com.app.base.compose.design.theme.AppTheme
import com.app.base.compose.dialog.facility.DialogFrame
import com.app.base.compose.dialog.facility.DialogStyle

@Composable
internal fun AlertDialogContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    title: @Composable (() -> Unit)? = null,
    positiveButton: @Composable (() -> Unit)? = null,
    negativeButton: @Composable (() -> Unit)? = null,
    dialogStyle: DialogStyle = DialogStyle.defaultImpl().copy(contentPadding = PaddingValues(0.dp)),
) {
    DialogFrame(
        modifier = modifier,
        dialogStyle = dialogStyle,
    ) {
        Column {
            title?.let {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    it()
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F, fill = false),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
            AlertButtons(positiveButton, negativeButton)
        }
    }
}

@Composable
private fun AlertButtons(
    positiveButton: @Composable (() -> Unit)?,
    negativeButton: @Composable (() -> Unit)?,
) {
    if (negativeButton != null || positiveButton != null) {
        HorizontalDivider()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        negativeButton?.let {
            Box(modifier = Modifier.weight(1F), contentAlignment = Alignment.Center) {
                it()
            }
            VerticalDivider()
        }

        positiveButton?.let {
            Box(modifier = Modifier.weight(1F), contentAlignment = Alignment.Center) {
                it()
            }
        }
    }
}

@Composable
internal fun AlertDialogContent(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleStyle: TextStyle = AlertDialogDefaults.DefaultTitleStyle,
    message: String,
    messageStyle: TextStyle = AlertDialogDefaults.DefaultMessageStyle,
    positiveButton: String? = null,
    onPositiveClick: (() -> Unit)? = null,
    negativeButton: String? = null,
    onNegativeClick: (() -> Unit)? = null,
    dialogStyle: DialogStyle = DialogStyle.defaultImpl().copy(contentPadding = PaddingValues(0.dp)),
) {
    DialogFrame(
        modifier = modifier,
        dialogStyle = dialogStyle,
    ) {
        Column {
            // title
            title?.let {
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(it, style = titleStyle)
                }
            }

            // content
            val hasButton = negativeButton != null || positiveButton != null
            val contentPaddingValue = PaddingValues(
                top = if (title == null) 30.dp else 10.dp,
                start = dimensionResource(com.app.base.ui.theme.R.dimen.common_page_edge_big),
                end = dimensionResource(com.app.base.ui.theme.R.dimen.common_page_edge_big),
                bottom = if (hasButton) 10.dp else 20.dp
            )
            Box(
                modifier = Modifier
                    .padding(contentPaddingValue)
                    .fillMaxWidth()
                    .weight(1F, fill = false),
                contentAlignment = Alignment.Center
            ) {
                Text(text = message, style = messageStyle, modifier = Modifier.verticalScroll(rememberScrollState()))
            }

            // buttons
            if (hasButton) {
                AlertButtons(
                    positiveButton = positiveButton,
                    onPositiveClick = onPositiveClick,
                    negativeButton = negativeButton,
                    onNegativeClick = onNegativeClick,
                )
            }
        }
    }
}

@Composable
private fun AlertButtons(
    positiveButton: String? = null,
    onPositiveClick: (() -> Unit)? = null,
    negativeButton: String? = null,
    onNegativeClick: (() -> Unit)? = null,
) {
    HorizontalDivider()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        negativeButton?.let {
            Box(modifier = Modifier.weight(1F), contentAlignment = Alignment.Center) {
                TextButton(
                    onClick = {
                        onNegativeClick?.invoke()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors().copy(
                        contentColor = AppTheme.colors.textLevel2
                    )
                ) {
                    Text(it)
                }
            }
            VerticalDivider()
        }

        positiveButton?.let {
            Box(modifier = Modifier.weight(1F), contentAlignment = Alignment.Center) {
                TextButton(
                    onClick = {
                        onPositiveClick?.invoke()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(it)
                }
            }
        }
    }
}