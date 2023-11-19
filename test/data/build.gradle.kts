plugins {
    id("com.deskbird.android.lib")
}

android {
    namespace = "com.deskbird.test.data"
}

dependencies{
    implementation(project(":domain:api"))
}