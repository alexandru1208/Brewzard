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
    implementation(project(":ui-design-system"))
    implementation(project(":strings"))
    implementation(project(":domain"))
    implementation(project(":datasource-remote"))
    implementation(project(":datasource-local"))
    implementation(project(":feature-breweries-list:ui"))
    implementation(project(":feature-breweries-favorites-list:ui"))
}