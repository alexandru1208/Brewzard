plugins {
    id("com.deskbird.android.app")
    id("com.deskbird.android.di")
    id("com.deskbird.android.compose")
}

android {
    namespace = "com.deskbird.brewzard"

    defaultConfig {
        applicationId = "com.deskbird.brewzard"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":ui:design-system"))
    implementation(project(":ui:strings"))
    implementation(project(":domain:api"))
    implementation(project(":domain:impl"))
    implementation(project(":datasource:remote"))
    implementation(project(":datasource:local"))
    implementation(project(":feature:breweries-list:ui"))
    implementation(project(":feature:breweries-favorites:ui"))
    implementation(project(":feature:breweries-details:ui"))
}