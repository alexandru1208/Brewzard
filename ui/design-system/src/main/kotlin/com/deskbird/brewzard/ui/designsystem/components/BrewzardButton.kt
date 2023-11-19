package com.deskbird.brewzard.ui.designsystem.components

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.deskbird.brewzard.ui.designsystem.theme.BrewzardTheme

@Composable
fun BrewzardButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
) = Button(
    modifier = modifier,
    onClick = onClick,
) {
    Text(text = text, style = MaterialTheme.typography.labelLarge)
}

@Composable
@Preview
private fun Preview() = BrewzardTheme {
    BrewzardButton(text = "Click me")
}
