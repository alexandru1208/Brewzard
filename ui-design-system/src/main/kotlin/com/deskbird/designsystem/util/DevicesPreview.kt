package com.deskbird.designsystem.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Pixel 7 Portrait Day", device = "id:pixel_7", showSystemUi = true,
)
@Preview(
    name = "Pixel 7 Portrait Night", device = "id:pixel_7", showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Pixel 7 Landscape",
    device = "spec:parent=pixel_7,orientation=landscape",
    showSystemUi = true
)
@Preview(name = "Nexus S Portrait", device = "spec:parent=Nexus S", showSystemUi = true)
@Preview(
    name = "Pixel C Portrait",
    device = "spec:parent=pixel_c,orientation=portrait",
    showSystemUi = true
)
@Preview(name = "Pixel C Landscape", device = "spec:parent=pixel_c", showSystemUi = true)
annotation class DevicePreview