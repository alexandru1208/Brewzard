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

rootProject.name = "brewzard"
include(":app")
include(":domain:api")
include(":domain:impl")
include(":datasource:remote")
include(":datasource:local")
include(":ui:strings")
include(":ui:design-system")
include(":ui:util")
include(":test:data")
include(":test:util")
include(":feature:breweries-list:domain")
include(":feature:breweries-list:ui")
include(":feature:breweries-favorites:domain")
include(":feature:breweries-favorites:ui")
include(":feature:breweries-details:domain")
include(":feature:breweries-details:ui")
