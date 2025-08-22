import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "net.swofty"
version = "3.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":service.api"))
    implementation(project(":service.generic"))
}

application {
    mainClass.set("net.swofty.service.experimentation.ExperimentationService")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ServiceExperiments")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}