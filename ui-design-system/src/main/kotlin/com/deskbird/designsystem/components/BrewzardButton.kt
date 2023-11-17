package com.deskbird.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deskbird.designsystem.theme.BrewzardTheme

@Composable
fun BrewzardButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) = Button(
    modifier = modifier,
    onClick = onClick
) {
    Text(text = text, style = MaterialTheme.typography.labelLarge)
}

@Composable
@Preview
private fun Preview() = BrewzardTheme {
    BrewzardButton(text = "Click me")
}