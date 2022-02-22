plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
}

group = "com.tobrun.datacompat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

ksp {
    arg("autoserviceKsp.verify", "true")
    arg("autoserviceKsp.verbose", "true")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")
    implementation("com.google.auto.service:auto-service-annotations:1.0.1")
    implementation("com.squareup:kotlinpoet:1.10.2")
    implementation("com.squareup:kotlinpoet-ksp:1.10.2")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")

    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.7")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview"
}