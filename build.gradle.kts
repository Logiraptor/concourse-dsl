import org.jetbrains.kotlin.psi.addRemoveModifier.addAnnotationEntry

plugins {
    application
    kotlin("jvm") version "1.2.61"
    wrapper
    idea
}

apply {
    plugin("kotlin-kapt")
}

application {
    mainClassName = "io.poyarzun.concoursedsl.Fresh"
}

val arrowVersion = "0.7.3"
fun DependencyHandler.arrow(module: String): Any =
    "io.arrow-kt:arrow-$module:$arrowVersion"

dependencies {
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("com.fasterxml.jackson.core:jackson-databind:2.7.1-1")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.7.1-2")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.7.1")
    compile(arrow("core"))
    compile(arrow("syntax"))
    compile(arrow("typeclasses"))
    compile(arrow("data"))
    compile(arrow("instances-core"))
    compile(arrow("instances-data"))
    kapt(arrow("annotations-processor"))

    compile(arrow("free")) //optional
    compile(arrow("mtl")) //optional
    compile(arrow("effects")) //optional
    compile(arrow("effects-rx2")) //optional
    compile(arrow("effects-reactor")) //optional
    compile(arrow("effects-kotlinx-coroutines")) //optional
    compile(arrow("optics")) //optional
    compile(arrow("generic")) //optional
    compile(arrow("recursion")) //optional
}

repositories {
    jcenter()
}

//buildscript {
//    ext.kotlin_version = "1.2.51"
//
//    repositories {
//        mavenCentral()
//    }
//
//    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
//    }
//}
//
//plugins {
//    id 'org.jetbrains.kotlin.jvm' version '1.2.51'
//}
//
//apply plugin: "kotlin"
//apply plugin: "wrapper"
//
//repositories {
//    mavenCentral()
//}
//dependencies {
//    compile "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
//    compile 'com.fasterxml.jackson.core:jackson-databind:2.7.1-1'
//    compile 'com.fasterxml.jackson.module:jackson-module-kotlin:2.7.1-2'
//    compile 'com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.7.1'
//}
//
//compileKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
//compileTestKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
