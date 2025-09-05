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
    mavenCentral()
}

dependencies {
    implementation(project(":service.generic"))
    implementation(project(":commons"))
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    implementation("org.tinylog:tinylog-api:2.7.0")
    implementation("org.tinylog:tinylog-impl:2.7.0")
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