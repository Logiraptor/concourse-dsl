import java.net.URI
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.3.0"
    wrapper
    idea
    `java-library`
    `maven-publish`
    signing
    application
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.31"
}

configure<NoArgExtension> {
    annotation("io.poyarzun.concoursedsl.domain.NoArg")
}

group = "io.poyarzun"
version = "0.4.0"

repositories {
    jcenter()
}

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("script-runtime", kotlinVersion))
    implementation(kotlin("compiler-embeddable", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))

    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.5")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.5")
    implementation("com.squareup:kotlinpoet:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

val javadoc by tasks

val javadocJar by tasks.creating(Jar::class) {
    classifier = "javadoc"
    from(javadoc)
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(kotlin.sourceSets["main"].kotlin)
}

val sonatypePassword: String? by project
val sonatypeUsername: String? by project

publishing {
    publications {
        create("ProductionJar", MavenPublication::class.java) {

            pom {
                name.set("Concourse DSL")
                description.set("A kotlin DSL for configuring Concourse pipelines")
                url.set("https://github.com/Logiraptor/concourse-dsl")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                scm {
                    url.set("https://github.com/Logiraptor/concourse-dsl")
                }
                developers {
                    developer {
                        id.set("poyarzun")
                        name.set("Patrick Oyarzun")
                        email.set("patrick@poyarzun.io")
                    }
                }
            }

            from(components["java"])

            artifact(sourcesJar)
            artifact(javadocJar)
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

application {
    mainClassName = "io.poyarzun.concoursedsl.printer.PrinterKt"
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "io.poyarzun.concoursedsl.printer.PrinterKt"
    }
    from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}

