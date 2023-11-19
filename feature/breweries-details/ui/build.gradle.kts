plugins {
    id("com.deskbird.android.brewzard.feature.ui")
}

android {
    namespace = "com.deskbird.brewzard.breweries.details.ui"
}

dependencies {
    implementation(project(":feature:breweries-details:domain"))
}