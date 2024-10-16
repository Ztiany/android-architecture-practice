package com.app.sample.compose.ui.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.base.compose.design.component.button.DeepestTextButton
import com.app.base.compose.design.component.button.LightestTextButton
import com.app.base.compose.design.component.button.PrimaryGradientButton
import com.app.base.compose.design.component.button.PrimaryOutlinedButton
import com.app.sample.compose.widget.SimpleScaffold

@Composable
fun ButtonsScreen() {
    SimpleScaffold(title = "Button Component") {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                }) {
                Text("Button")
            }

            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = false
            ) {
                Text("Button (Disabled)")
            }

            PrimaryGradientButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                }) {
                Text("PrimaryGradientButton")
            }

            PrimaryGradientButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = false
            ) {
                Text("PrimaryGradientButton (Disabled)")
            }

            TextButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
            ) {
                Text("TextButton")
            }

            TextButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = false
            ) {
                Text("TextButton (Disabled)")
            }

            LightestTextButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
            ) {
                Text("LightestTextButton")
            }

            LightestTextButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = false
            ) {
                Text("LightestTextButton (Disabled)")
            }

            DeepestTextButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
            ) {
                Text("DeepestTextButton")
            }

            DeepestTextButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = false
            ) {
                Text("DeepestTextButton (Disabled)")
            }

            OutlinedButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
            ) {
                Text("OutlinedButton")
            }

            OutlinedButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = false
            ) {
                Text("OutlinedButton (Disabled)")
            }

            PrimaryOutlinedButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
            ) {
                Text("OutlinedButton")
            }

            PrimaryOutlinedButton(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onClick = {

                },
                enabled = false
            ) {
                Text("OutlinedButton (Disabled)")
            }
        }
    }
}