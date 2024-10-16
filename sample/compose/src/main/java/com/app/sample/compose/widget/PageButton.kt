package com.app.sample.compose.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PageButton(text: String, onClick: () -> Unit) {
    Button(
        onClick, modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(text = text)
    }
}