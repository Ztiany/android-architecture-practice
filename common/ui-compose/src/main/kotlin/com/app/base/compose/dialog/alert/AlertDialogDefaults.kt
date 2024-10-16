package com.app.base.compose.dialog.alert

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.app.base.compose.design.theme.AppTheme

object AlertDialogDefaults {

    val DefaultTitleStyle: TextStyle
        @Composable get() = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textLevel1,
            textAlign = TextAlign.Center
        )

    val DefaultMessageStyle: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = AppTheme.colors.textLevel1,
            textAlign = TextAlign.Center
        )

}
