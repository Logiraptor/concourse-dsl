import org.gradle.api.tasks.bundling.Jar

plugins {
    kotlin("jvm") version "1.3.0-rc-190"
    wrapper
    idea
    `maven-publish`
}

group = "io.poyarzun"
version = "0.0.1"

repositories {
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("main-kts"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.5")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.5")
}