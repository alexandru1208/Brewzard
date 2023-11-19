plugins {
    id("com.deskbird.android.brewzard.feature.ui")
}

android {
    namespace = "com.deskbird.brewzard.breweries.favorites.ui"
}

dependencies {
    implementation(project(":feature:breweries-favorites:domain"))
}