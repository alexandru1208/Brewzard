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

rootProject.name = "Brewzard"
include(":app")
include(":strings")
include(":domain")
include(":datasource-remote")
include(":datasource-local")
include(":ui-design-system")
include(":ui-util")
include(":feature-breweries-list:domain")
include(":feature-breweries-list:ui")
include(":feature-breweries-favorites-list:domain")
include(":feature-breweries-favorites-list:ui")
include(":feature-breweries-details:domain")
include(":feature-breweries-details:ui")
