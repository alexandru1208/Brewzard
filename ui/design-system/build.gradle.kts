plugins {
    id("com.deskbird.android.lib")
    id("com.deskbird.android.compose")
}

android {
    namespace = "com.deskbird.brewzard.ui.designsystem"
}

dependencies {
    implementation(project(":ui:strings"))
}

