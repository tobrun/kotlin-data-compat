plugins {
    kotlin("jvm")
	id("maven-publish")
}

group = "com.tobrun.datacompat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
}

project.apply {
	from("$rootDir/gradle/publish.gradle")
}