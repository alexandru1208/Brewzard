plugins {
    id("com.deskbird.android.lib")
}

android {
    namespace = "com.deskbird.brewzard.test.util"
}

dependencies {
    implementation(libs.bundles.testing)
}