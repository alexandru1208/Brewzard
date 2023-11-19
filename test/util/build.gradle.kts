plugins {
    id("com.deskbird.android.lib")
}

android {
    namespace = "com.deskbird.test.util"
}

dependencies {
    implementation(libs.bundles.testing)
}