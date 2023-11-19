plugins {
    id("com.deskbird.android.lib")
}

android {
    namespace = "com.deskbird.brewzard.test.data"
}

dependencies{
    implementation(project(":domain:api"))
}