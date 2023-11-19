plugins {
    id("com.deskbird.android.brewzard.feature.ui")
}

android {
    namespace = "com.deskbird.brewzard.breweries.list.ui"
}

dependencies{
    implementation(project(":feature:breweries-list:domain"))
}