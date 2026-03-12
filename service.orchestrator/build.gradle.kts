import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("io.github.goooler.shadow") version "8.1.8"
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
    implementation(libs.caffeine)
    implementation(libs.tinylogApi)
    implementation(libs.tinylogImpl)
}

application {
    mainClass.set("net.swofty.service.orchestrator.OrchestratorService")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ServiceOrchestrator")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}