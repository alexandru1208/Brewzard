package com.deskbird.designsystem.components

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deskbird.designsystem.theme.BrewzardTheme
import com.deskbird.strings.R

@Composable
fun BrewzardError(
    modifier: Modifier = Modifier,
    message: String,
    onTryAgainClick: () -> Unit = {}
) = Column(
    modifier = modifier.padding(16.dp),
    verticalArrangement = spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.something_went_wrong),
        style = typography.titleLarge,
        color = colorScheme.error
    )
    Text(
        textAlign = TextAlign.Center,
        text = message,
        style = typography.bodyLarge,
        color = colorScheme.error
    )
    BrewzardButton(text = stringResource(id = R.string.try_again), onClick = onTryAgainClick)
}


@Composable
@Preview
private fun Preview() = BrewzardTheme {
    BrewzardError(message = "An error occurred while performing the action!")
}