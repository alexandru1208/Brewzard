plugins {
    id("com.deskbird.android.lib")
    id("com.deskbird.android.di")
}

android {
    namespace = "com.deskbird.domain.impl"
}

dependencies {
    implementation(project(":domain:api"))
    testImplementation(project(":test:util"))
    testImplementation(project(":test:data"))
}