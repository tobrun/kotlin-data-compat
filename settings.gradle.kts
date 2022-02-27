pluginManagement {
    val kotlinVersion = "1.6.10"
    val kspVersion = "1.6.10-1.0.2"
    repositories {
        gradlePluginPortal()
        google()
    }
    plugins {
        id("com.google.devtools.ksp") version kspVersion
        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "data-compat"

include(":annotation")
include(":processor")
include(":example")