pluginManagement {
    includeBuild("buildLogic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = rootProject.projectDir.name
include(fileTree(rootProject.projectDir)
    .filter { it.name == "build.gradle.kts" }
    .map { it.parent.replace(rootProject.projectDir.path, "").replace("/", ":") }
    .filter { !it.contains("buildLogic") || !it.contains("buildSrc") }
)
