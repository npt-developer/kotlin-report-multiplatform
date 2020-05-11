pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:3.5.2")
            }
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:3.5.2")
            }
            if (requested.id.id == "org.jetbrains.kotlin.multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
            }

            if (requested.id.id == "com.squareup.sqldelight") {
                useModule("com.squareup.sqldelight:gradle-plugin:1.3.0")
            }
        }
    }

    repositories {
        gradlePluginPortal()
        google()
        jcenter()
    }
}

// https://cashapp.github.io/sqldelight/multiplatform/
// Multiplatform requires the gradle metadata feature, which you need to enable via the settings.gradle file in the project root
enableFeaturePreview("GRADLE_METADATA")

include(":shared")
include(":androidApp")
