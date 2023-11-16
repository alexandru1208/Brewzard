package com.deskbird.designsystem.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Pixel 7 Portrait Day",
    device = "id:pixel_7",
)
@Preview(
    name = "Pixel 7 Portrait Night",
    device = "id:pixel_7",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Pixel 7 Landscape",
    device = "spec:parent=pixel_7,orientation=landscape",
)
@Preview(
    name = "Nexus S Portrait",
    device = "spec:parent=Nexus S"
)
@Preview(
    name = "Pixel C Portrait",
    device = "spec:parent=pixel_c,orientation=portrait",
)
@Preview(
    name = "Pixel C Landscape",
    device = "spec:parent=pixel_c"
)
annotation class DevicePreview