plugins {
    id("com.deskbird.android.lib")
    id("com.deskbird.android.compose")
}

android {
    namespace = "com.deskbird.ui.designsystem"
}

dependencies {
    implementation(project(":strings"))
}

