import org.gradle.api.publish.maven.internal.publisher.MavenRemotePublisher
import org.gradle.api.tasks.bundling.Jar
import java.net.URI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.0-rc-190"
    wrapper
    idea
    `java-library`
    `maven-publish`
    signing
}

group = "io.poyarzun"
version = "0.0.1-SNAPSHOT"

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

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

//val javadoc by tasks
//
//val javadocJar by tasks.creating(Jar::class) {
//    classifier = "javadoc"
//    from(javadoc)
//}
//
//val sourcesJar by tasks.creating(Jar::class) {
//    classifier = "sources"
//    from(sourceSets["main"].allSource)
//}

val sonatypePassword: String by project
val sonatypeUsername: String by project

publishing {
    publications {
        create("ProductionJar", MavenPublication::class.java) {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "Central"
            url = when {
                version.toString().endsWith("SNAPSHOT") -> URI("https://oss.sonatype.org/content/repositories/snapshots/")
                else -> URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                password = sonatypePassword
                username = sonatypeUsername
            }
        }
    }
}

signing {
    sign(publishing.publications["ProductionJar"])
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}