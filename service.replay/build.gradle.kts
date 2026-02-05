import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.1"
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation(project(":service.generic"))
    implementation(project(":commons"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.mongodb:bson:5.6.2")
    implementation("org.mongodb:mongodb-driver-sync:5.6.2")

    // Compression libraries
    implementation("at.yawk.lz4:lz4-java:1.10.3")
    implementation("com.github.luben:zstd-jni:1.5.7-3")
}

application {
    mainClass.set("net.swofty.service.replay.ReplayService")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ServiceReplay")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}
