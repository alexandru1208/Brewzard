plugins {
    id("com.deskbird.android.brewzard.feature.ui")
}

android {
    namespace = "com.deskbird.breweries.favorites.list.ui"
}

dependencies {
    implementation(project(":feature:breweries-favorites-list:domain"))
}