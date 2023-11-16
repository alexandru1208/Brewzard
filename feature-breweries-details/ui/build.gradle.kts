plugins {
    id("com.deskbird.android.brewzard.feature.ui")
}

android {
    namespace = "com.deskbird.breweries.details.ui"
}

dependencies {
    implementation(project(":feature-breweries-details:domain"))
}