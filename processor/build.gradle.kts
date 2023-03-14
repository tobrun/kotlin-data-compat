plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
	id("maven-publish")
}

kotlin{
    jvmToolchain(11)
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
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.10-1.0.9")
    implementation("com.google.auto.service:auto-service-annotations:1.0.1")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    ksp("dev.zacsweers.autoservice:auto-service-ksp:1.0.0")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")

    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.5.0")

	implementation(project(":annotation"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    kotlinOptions.freeCompilerArgs += "-Xopt-in=com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview"
}

project.apply {
	from("$rootDir/gradle/publish.gradle")
}