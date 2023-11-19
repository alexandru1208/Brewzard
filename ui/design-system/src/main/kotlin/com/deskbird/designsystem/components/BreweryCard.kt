package com.deskbird.designsystem.components

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deskbird.designsystem.theme.BrewzardTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreweryCard(
    modifier: Modifier = Modifier,
    name: String,
    city: String,
    country: String,
    breweryType: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Card(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(8.dp),
        ) {
            Column(modifier = Modifier.weight(1.0f), verticalArrangement = spacedBy(4.dp)) {
                Text(text = name, style = typography.headlineSmall)
                Text(text = breweryType, style = typography.titleLarge)
                Text(text = "$city, $country", style = typography.bodyLarge)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    if (isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = colorScheme.primary,
                )
            }
        }
    }
}

@Composable
@Preview(name = "Not favorite brewery")
private fun NotFavoritePreview() = BrewzardTheme {
    BreweryCard(
        name = "(405) Brewing Co",
        city = "Norman",
        country = "United States",
        breweryType = "Micro",
        isFavorite = false,
    )
}

@Composable
@Preview(name = "Favorite  brewery")
private fun FavoritePreview() = BrewzardTheme {
    BreweryCard(
        name = "(405) Brewing Co",
        city = "Norman",
        country = "United States",
        breweryType = "Micro",
        isFavorite = true,
    )
}
